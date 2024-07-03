package com.ssafy.urturn.grading.service;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ssafy.urturn.grading.GradeStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExecutionService {


    private final GradeRepository gradeRepository;

    private static final String SOLUTIONFILEROOTDIR = "src/main/resources/tmpFiles/";
    private static final int TIMELIMIT = 10; // 10 seconds
    private static final int MEMORYLIMIT = 256 * 1024 * 1024; // 256 MB


//    @Async
    public void execute(Grade grade){
         if (checkCodeValidation(grade.getSourceCode())){
            gradeRepository.save(grade.updateStatus(EXEC_FORMAT_ERROR));
            log.info("{}", EXEC_FORMAT_ERROR.getDescription());
            return;
        }

        if (!compileCode(grade)){
            gradeRepository.save(grade.updateStatus(COMPILATION_ERROR));
            log.info("{}", COMPILATION_ERROR.getDescription());
            deleteFile(grade);
            return;
        }

        runCode(grade);
        deleteFile(grade);

    }

    private boolean compileCode(Grade grade) {
        try {
            makeFile(grade);
            String javaFilePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/Main.java";
            ProcessBuilder pb = new ProcessBuilder("javac", javaFilePath);
            Process process = pb.start();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            log.error("{}", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void makeFile(Grade grade) throws IOException {
        String dirPath = SOLUTIONFILEROOTDIR + grade.getToken();
        File dir = new File(dirPath);
        dir.mkdir();
        String filePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/Main.java";
        File file = new File(filePath);
        if (file.createNewFile()){
            Files.writeString(Paths.get(filePath), grade.getSourceCode());
        } else {
            throw new RuntimeException("이미 파일이 있는거같아..");
        }
    }

    private static void deleteFile(Grade grade) {
        try {
            Path javaFilePath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/Main.java");
            Path classPath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/Main.class");
            if (Files.exists(javaFilePath)) {
                Files.delete(javaFilePath);
            }
            if (Files.exists(classPath)) {
                Files.delete(classPath);
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private boolean checkCodeValidation(String sourceCode) {
        Pattern pattern = Pattern.compile("(?<!\\w)(Runtime\\.getRuntime\\(\\)\\.exec\\(\"[^\"]+\"\\)|ProcessBuilder\\s*\\([^)]+\\))");
        Matcher matcher = pattern.matcher(sourceCode);
        return matcher.find();
    }

    private void runCode(Grade grade) {
        try {
            String filePath = SOLUTIONFILEROOTDIR + grade.getToken();
            ProcessBuilder pb = new ProcessBuilder("java", "-Xmx" + MEMORYLIMIT, "-Xss256k", "-cp", filePath, "Main");
            Process process = pb.start();
            writeInput(grade, process);

            // Time Limit 체크
            boolean finished = process.waitFor(TIMELIMIT, TimeUnit.SECONDS); // 10초 이내에 종료되지 않으면 false 반환
            if (!finished) {
                process.destroy(); // 프로세스 강제 종료
                gradeRepository.save(grade.updateStatus(TIME_LIMIT_EXCEEDED));
                return;
            }

            // Runtime Error 체크
            int exitValue = process.exitValue();
            if (exitValue != 0) {
                InputStream errorStream = process.getErrorStream();
                String errorMessage = readOutput(errorStream);
                gradeRepository.save(grade.updateRuntimeErrorStatus(errorMessage));
                return;
            }

            // 정답 여부 체크
            evaluateOutput(grade, process);

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void evaluateOutput(Grade grade, Process process) {
        InputStream inputStream = process.getInputStream();
        String actualOutput = readOutput(inputStream);
        log.info("output : {}", actualOutput);
        log.info("ExpectedOutput : {}", grade.getExpectedOutput());

        if (actualOutput.equals(grade.getExpectedOutput())) {
            gradeRepository.save(grade.updateStatus(ACCEPTED));
        } else {
            gradeRepository.save(grade.updateStatus(WRONG_ANSWER));
        }
    }

    private static void writeInput(Grade grade, Process process) throws IOException {
        BufferedOutputStream br = new BufferedOutputStream(process.getOutputStream());
        log.info("stdin : {}", grade.getStdin());
        br.write(grade.getStdin().getBytes());
        br.flush();
        br.close();
    }

    private String readOutput(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString().trim();
    }


}
