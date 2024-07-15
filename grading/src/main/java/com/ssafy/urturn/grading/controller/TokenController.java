package com.ssafy.urturn.grading.controller;

import com.ssafy.urturn.grading.dto.request.GradeBatchCreateRequest;
import com.ssafy.urturn.grading.dto.response.TokenResponse;
import com.ssafy.urturn.grading.service.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/submissions/batch")
@RequiredArgsConstructor
public class TokenController {

    private final GradeService gradeService;

    @PostMapping("")
    public ResponseEntity<List<TokenResponse>> createTokens(@Valid @RequestBody GradeBatchCreateRequest gradeBatchCreateRequest){
        return ResponseEntity.ok(gradeService.createGrades(gradeBatchCreateRequest.submissions())
                    .stream().map(TokenResponse::from)
                    .toList());
    }
}
