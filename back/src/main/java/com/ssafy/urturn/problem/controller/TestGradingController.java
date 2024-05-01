package com.ssafy.urturn.problem.controller;

import com.ssafy.urturn.problem.dto.TestGetResultDto;
import com.ssafy.urturn.problem.service.GradingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/grade")
@RequiredArgsConstructor
public class TestGradingController {

    private final GradingService gradingService;

    @PostMapping("/getResult")
    public ResponseEntity<?> getResult(@RequestBody TestGetResultDto inputCode)
        throws InterruptedException {
        return ResponseEntity.ok(gradingService.getResult(inputCode.getProblemId(), inputCode.getInputCode()));
    }

}
