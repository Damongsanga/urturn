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
@Slf4j
public class CExecutionStrategy extends AbstractBasicStrategy {

    public CExecutionStrategy(GradeRepository gradeRepository) {
        super(gradeRepository);
    }

    @Async
    @Override
    public CompletableFuture<TokenWithStatus> execute(Grade grade){

        try{
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
            return CompletableFuture.completedFuture(TokenWithStatus.from(grade.getToken(), status));
        } finally {
            deleteFile(grade);
        }
    }

    private boolean compileCode(Grade grade) {
        try {
            makeFile(grade);
//            String cFilePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/example.c";
//            String complieFilePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/example";
//            ProcessBuilder pb = new ProcessBuilder("gcc", cFilePath, "-o", complieFilePath);
            String cFilePath = SOLUTIONFILEROOTDIR + grade.getToken();
            ProcessBuilder pb = new ProcessBuilder(
                    "docker", "run", "--rm", "-v", cFilePath + "/:/app/", "gcc:latest",
                    "gcc", "/app/example.c", "-o", "/app/example"
            );
            Process process = pb.start();
            process.waitFor();
            return process.exitValue() == 0;
        } catch (IOException | InterruptedException e) {
            log.info("{}", e.getMessage());
            return false;
        }
    }

    @Override
    protected void makeFile(Grade grade) {
        makeDir(grade);

        String filePath = SOLUTIONFILEROOTDIR + grade.getToken() + "/example.c";
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
            Path cFilePath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/example.c");
            Path compileFilePath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken() + "/example");
            Path dirPath = Paths.get(SOLUTIONFILEROOTDIR + grade.getToken());
            Files.deleteIfExists(cFilePath);
            Files.deleteIfExists(compileFilePath);
            Files.deleteIfExists(dirPath);
        } catch (IOException e){
            throw new CustomException(FILE_DELETE_ERROR);
        }
    }


    @Override
    protected GradeStatus runCode(Grade grade) {
        try {
//            ProcessBuilder pb = new ProcessBuilder(filePath+"/example");
            String filePath = SOLUTIONFILEROOTDIR + grade.getToken();
            ProcessBuilder pb = new ProcessBuilder("docker", "run", "--memory="+MEMORYLIMIT+"mb", "--rm", "-i", "-v", filePath + "/:/app", "gcc:latest",
                    "/app/example");
            Process process = pb.start();
            writeInput(grade, process);

            return checkAndSaveStatus(grade, process);

        } catch (IOException | InterruptedException e) {
            gradeRepository.save(grade.updateStatus(INTERNAL_ERROR));
            throw new CustomException(RUN_CODE_ERROR);
        }
    }

}
