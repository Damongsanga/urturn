package com.ssafy.urturn.problem.controller;

import com.ssafy.urturn.problem.dto.GradingResponse;
import com.ssafy.urturn.problem.dto.ProblemTestcaseDto;
import com.ssafy.urturn.problem.dto.TestGetResultDto;
import com.ssafy.urturn.problem.dto.TestSelectProblemDto;
import com.ssafy.urturn.problem.service.GradingService;
import com.ssafy.urturn.problem.service.ProblemService;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/grade")
@RequiredArgsConstructor
public class TestGradingController {

    private final GradingService gradingService;
    private final ProblemService problemService;

    @PostMapping("/getResult")
    public ResponseEntity<GradingResponse> getResult(@RequestBody TestGetResultDto inputCode) {
        return ResponseEntity.ok(gradingService.getResult(inputCode.getProblemId(), inputCode.getInputCode(), inputCode.getLanguage()));
    }

    @PostMapping("/select2Problem")
    public ResponseEntity<List<ProblemTestcaseDto>> getResult(@RequestBody TestSelectProblemDto dto) {
        return ResponseEntity.ok(problemService.getTwoProblem(dto.getMemberId(), dto.getPairId(), dto.getLevel()));
    }

}
