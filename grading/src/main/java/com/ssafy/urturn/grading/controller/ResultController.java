package com.ssafy.urturn.grading.controller;

import com.ssafy.urturn.grading.controller.response.GradeResponse;
import com.ssafy.urturn.grading.service.GradeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/submissions/batch")
@RequiredArgsConstructor
public class ResultController {

    private final GradeQueryService gradeQueryService;

    @GetMapping("")
    public ResponseEntity<List<GradeResponse>> getResults(List<String> tokens){
        return ResponseEntity.ok(gradeQueryService.getGrades(tokens)
                .stream().map(GradeResponse::from)
                .toList());
    }

}
