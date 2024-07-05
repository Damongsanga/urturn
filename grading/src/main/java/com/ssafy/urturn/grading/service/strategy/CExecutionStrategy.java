package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.global.exception.CustomException;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ssafy.urturn.grading.domain.GradeStatus.*;
import static com.ssafy.urturn.grading.global.exception.CommonErrorCode.INTERNAL_SERVER_ERROR;
import static com.ssafy.urturn.grading.global.exception.CustomErrorCode.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class CExecutionStrategy implements ExecutionStrategy {

    private final GradeRepository gradeRepository;

    private static final String SOLUTIONFILEROOTDIR = "src/main/resources/tmpFiles/";
    private static final int TIMELIMIT = 10; // 10 seconds
    private static final int MEMORYLIMIT = 256 * 1024 * 1024; // 256 MB

//    @Async
    @Override
    public CompletableFuture<TokenWithStatus> execute(Grade grade){

         if (checkCodeValidation(grade.getSourceCode())){
            gradeRepository.save(grade.updateStatus(EXEC_FORMAT_ERROR));
            log.info("{}", EXEC_FORMAT_ERROR.getDescription());
            return CompletableFuture.completedFuture(TokenWithStatus.from(grade.getToken(), EXEC_FORMAT_ERROR));
        }

        if (!compileCode(grade)){
            gradeRepository.save(grade.updateStatus(COMPILATION_ERROR));
            log.info("{}", COMPILATION_ERROR.getDescription());
            deleteFile(grade);
            return CompletableFuture.completedFuture(TokenWithStatus.from(grade.getToken(), COMPILATION_ERROR));
        }

        GradeStatus status = runCode(grade);
        deleteFile(grade);
        return CompletableFuture.completedFuture(TokenWithStatus.from(grade.getToken(), status));
    }

    private boolean compileCode(Grade grade) {
        try {
            makeFile(grade);
            String javaFilePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/example.c";
            ProcessBuilder pb = new ProcessBuilder("gcc", javaFilePath, "-o", "example");
            Process process = pb.start();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            log.error("{}", e.getMessage());
            return false;
        }
    }

    private static void makeFile(Grade grade) {
        makeDir(grade);

        String filePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/example";
        File file = new File(filePath);
        try{
            if (file.createNewFile()){
                Files.writeString(Paths.get(filePath), grade.getSourceCode());
            } else {
                throw new CustomException(INTERNAL_SERVER_ERROR, "내부에 동일한 이름의 파일이 존재합니다.");
            }
        } catch (IOException e){
            log.error("{}", e.getMessage());
            throw new CustomException(FILE_CREATE_ERROR);
        }
    }

    private static void makeDir(Grade grade) {
        String dirPath = SOLUTIONFILEROOTDIR + grade.getToken();
        File dir = new File(dirPath);
        dir.mkdir();
    }

    private static void deleteFile(Grade grade) {
        try {
            Path cFilePath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/example.c");
            Path compileFilePath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/example");
            Path dirPath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken());
            Files.deleteIfExists(cFilePath);
            Files.deleteIfExists(compileFilePath);
            Files.deleteIfExists(dirPath);
        } catch (IOException e){
            log.error("{}", e.getMessage());
            throw new CustomException(FILE_DELETE_ERROR);
        }
    }


    private boolean checkCodeValidation(String sourceCode) {
        Pattern pattern = Pattern.compile("(?<!\\w)(Runtime\\.getRuntime\\(\\)\\.exec\\(\"[^\"]+\"\\)|ProcessBuilder\\s*\\([^)]+\\))");
        Matcher matcher = pattern.matcher(sourceCode);
        return matcher.find();
    }

    private GradeStatus runCode(Grade grade) {
        try {
            String filePath = SOLUTIONFILEROOTDIR + grade.getToken();
            ProcessBuilder pb = new ProcessBuilder("."+filePath+"/example");
            Process process = pb.start();
            writeInput(grade, process);

            // Time Limit 체크
            boolean finished = process.waitFor(TIMELIMIT, TimeUnit.SECONDS); // 10초 이내에 종료되지 않으면 false 반환
            if (!finished) {
                process.destroy(); // 프로세스 강제 종료
                gradeRepository.save(grade.updateStatus(TIME_LIMIT_EXCEEDED));
                return TIME_LIMIT_EXCEEDED;
            }

            // Runtime Error 체크
            int exitValue = process.exitValue();
            if (exitValue != 0) {
                InputStream errorStream = process.getErrorStream();
                String errorMessage = readOutput(errorStream);
                gradeRepository.save(grade.updateRuntimeErrorStatus(errorMessage));
                return RUNTIME_ERROR_OTHER;
            }

            // 정답 여부 체크
            return evaluateOutput(grade, process);

        } catch (IOException | InterruptedException e) {
            log.error(e.getMessage());
            throw new CustomException(RUN_CODE_ERROR);
        }
    }

    private GradeStatus evaluateOutput(Grade grade, Process process) throws IOException {
        InputStream inputStream = process.getInputStream();
        String actualOutput = readOutput(inputStream);
        log.info("output : {}", actualOutput);
        log.info("ExpectedOutput : {}", grade.getExpectedOutput());

        if (actualOutput.equals(grade.getExpectedOutput())) {
            gradeRepository.save(grade.updateStatus(ACCEPTED));
            return ACCEPTED;
        } else {
            gradeRepository.save(grade.updateStatus(WRONG_ANSWER));
            return WRONG_ANSWER;
        }
    }

    private static void writeInput(Grade grade, Process process) throws IOException {
        try (BufferedOutputStream br = new BufferedOutputStream(process.getOutputStream())) {
            br.write(grade.getStdin().getBytes());
            br.flush();
        }
    }

    private String readOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }


}
