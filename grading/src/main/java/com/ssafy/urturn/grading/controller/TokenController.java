package com.ssafy.urturn.grading.controller;

import com.ssafy.urturn.grading.domain.request.GradeCreate;
import com.ssafy.urturn.grading.controller.response.TokenResponse;
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
    public ResponseEntity<List<TokenResponse>> createTokens(@Valid @RequestBody List<GradeCreate> gradeCreates){
        return ResponseEntity.ok(gradeService.createGrades(gradeCreates)
                    .stream().map(TokenResponse::from)
                    .toList());
    }
}
