package com.ssafy.urturn.grading.domain.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ssafy.urturn.grading.domain.GradeStatus.*;
import static com.ssafy.urturn.grading.domain.GradeStatus.WRONG_ANSWER;

@Component
@RequiredArgsConstructor
@Slf4j
public abstract class AbstractBasicStrategy implements ExecutionStrategy{

    protected final GradeRepository gradeRepository;

    protected static final String SOLUTIONFILEROOTDIR = "./src/main/resources/tmpFiles/";
    protected static final int TIMELIMIT = 10; // 10 seconds
    protected static final int MEMORYLIMIT = 256; // 256 MB

    @Async
    @Override
    public abstract CompletableFuture<TokenWithStatus> execute(Grade grade);

    protected abstract void makeFile(Grade grade);

    protected void makeDir(Grade grade) {
        String dirPath = SOLUTIONFILEROOTDIR + grade.getToken();
        File dir = new File(dirPath);
        dir.mkdir();
    }

    protected abstract void deleteFile(Grade grade);


    protected boolean checkCodeValidation(String sourceCode) {
        Pattern pattern = Pattern.compile("(?<!\\w)(Runtime\\.getRuntime\\(\\)\\.exec\\(\"[^\"]+\"\\)|ProcessBuilder\\s*\\([^)]+\\))");
        Matcher matcher = pattern.matcher(sourceCode);
        return matcher.find();
    }

    protected abstract GradeStatus runCode(Grade grade);

    protected GradeStatus evaluateOutput(Grade grade, Process process) throws IOException {
        String actualOutput = readOutput(process.getInputStream());
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

    protected void writeInput(Grade grade, Process process) throws IOException {
        try (BufferedOutputStream br = new BufferedOutputStream(process.getOutputStream())) {
            br.write(grade.getStdin().getBytes());
            br.flush();
        }
    }

    protected String readOutput(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString().trim();
    }

    protected GradeStatus checkAndSaveStatus(Grade grade, Process process) throws InterruptedException, IOException {
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
            log.info("RUNTIME ERROR : {}", errorMessage);
            return RUNTIME_ERROR_OTHER;
        }

        // 정답 여부 체크
        return evaluateOutput(grade, process);
    }

}
