package com.ssafy.urturn.problem.controller;

import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.problem.dto.ProblemCreateRequest;
import com.ssafy.urturn.problem.dto.TestcaseCreateRequest;
import com.ssafy.urturn.problem.service.ProblemService;
import com.ssafy.urturn.problem.service.S3UploadService;
import com.ssafy.urturn.problem.service.TestcaseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
@Slf4j
public class ProblemController {

    private final ProblemService problemService;
    private final TestcaseService testcaseService;
    private final S3UploadService s3UploadService;

    // validation 필요
    @PostMapping(value="", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createProblem(
            @RequestParam("title") String title,
            @RequestParam("content") MultipartFile file,
            @RequestParam("level") Level level){

        // 테스트 필요, 에러처리 필요
        String content = s3UploadService.saveFile(file, "problem/");

        ProblemCreateRequest req = ProblemCreateRequest.builder()
                .title(title)
                .content(content)
                .level(level)
                .build();

        return ResponseEntity.ok(problemService.createProblem(req));
    }

    // validation 필요
    @PostMapping("/testcase/{problemId}")
    public ResponseEntity<?> createTestcases(@RequestBody List<TestcaseCreateRequest> reqs, @PathVariable Long problemId){
        return ResponseEntity.ok(testcaseService.createTestcases(reqs, problemId));
    }

    @GetMapping("/{problemId}")
    public ResponseEntity<?> getProblem(@PathVariable Long problemId){
        return ResponseEntity.ok(problemService.getProblemWithPublicTestcase(problemId));
    }

}
