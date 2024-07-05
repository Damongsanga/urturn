package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.domain.GradeStatus;
import com.ssafy.urturn.grading.domain.repository.GradeRepository;
import com.ssafy.urturn.grading.global.exception.CustomException;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.ssafy.urturn.grading.domain.GradeStatus.*;
import static com.ssafy.urturn.grading.global.exception.CommonErrorCode.INTERNAL_SERVER_ERROR;
import static com.ssafy.urturn.grading.global.exception.CustomErrorCode.*;

@Component
@Slf4j
public class PythonExecutionStrategy extends AbstractBasicStrategy {

    public PythonExecutionStrategy(GradeRepository gradeRepository) {
        super(gradeRepository);
    }

    @Async
    @Override
    public CompletableFuture<TokenWithStatus> execute(Grade grade){
        try {
            if (checkCodeValidation(grade.getSourceCode())){
                gradeRepository.save(grade.updateStatus(EXEC_FORMAT_ERROR));
                log.info("{}", EXEC_FORMAT_ERROR.getDescription());
                return CompletableFuture.completedFuture(TokenWithStatus.from(grade.getToken(), EXEC_FORMAT_ERROR));
            }

            makeFile(grade);
            GradeStatus status = runCode(grade);
            return CompletableFuture.completedFuture(TokenWithStatus.from(grade.getToken(), status));
        } finally {
            deleteFile(grade);
        }
    }

    @Override
    protected void makeFile(Grade grade) {
        makeDir(grade);

        String filePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/Solution.py";
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

    @Override
    protected void deleteFile(Grade grade) {
        try {
            Path pythonFilePath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/Solution.py");
            Path dirPath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken());
            Files.deleteIfExists(pythonFilePath);
            Files.deleteIfExists(dirPath);
        } catch (IOException e){
            log.error("{}", e.getMessage());
            throw new CustomException(FILE_DELETE_ERROR);
        }
    }


    @Override
    protected GradeStatus runCode(Grade grade) {
        try {
            String filePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/Solution.py";
            ProcessBuilder pb = new ProcessBuilder("python3", filePath);
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
                log.info("RUNTIME ERROR : {}", errorMessage);
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

}
