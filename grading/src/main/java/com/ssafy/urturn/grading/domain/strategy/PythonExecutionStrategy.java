package com.ssafy.urturn.grading.domain.strategy;

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

import static com.ssafy.urturn.grading.domain.GradeStatus.*;
import static com.ssafy.urturn.grading.global.exception.code.CommonErrorCode.INTERNAL_SERVER_ERROR;
import static com.ssafy.urturn.grading.global.exception.code.CustomErrorCode.*;

@Component
@Slf4j
public class PythonExecutionStrategy extends AbstractBasicStrategy {

    private static final int MEMORYLIMIT = 512;

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
            gradeRepository.save(grade.updateStatus(INTERNAL_ERROR));
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
            throw new CustomException(FILE_DELETE_ERROR);
        }
    }


    @Override
    protected GradeStatus runCode(Grade grade) {
        try {
            String filePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/Solution.py";
//            ProcessBuilder pb = new ProcessBuilder("ulimit", "-v", MEMORYLIMIT+"" , "&&", "python3", filePath);
            ProcessBuilder pb = new ProcessBuilder("docker", "run", "--memory="+MEMORYLIMIT+"mb", "--rm", "-i", "-v", filePath+":/app/Solution.py", "python:3.9", "python3", "/app/Solution.py");

            Process process = pb.start();
            writeInput(grade, process);

            return checkAndSaveStatus(grade, process);

        } catch (IOException | InterruptedException e) {
            gradeRepository.save(grade.updateStatus(INTERNAL_ERROR));
            throw new CustomException(RUN_CODE_ERROR);
        }
    }

}
