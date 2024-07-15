package com.ssafy.urturn.grading.controller;

import com.ssafy.urturn.grading.dto.response.GradeBatchResponse;
import com.ssafy.urturn.grading.dto.response.GradeResponse;
import com.ssafy.urturn.grading.service.GradeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path= "/submissions/batch", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ResultController {

    private final GradeQueryService gradeQueryService;

    @GetMapping("")
    public ResponseEntity<GradeBatchResponse> getResults(
            @RequestParam(name = "base64_encoded", defaultValue = "false") boolean base64_encoded,
            @RequestParam(name = "tokens") String tokens,
            @RequestParam(name = "fields", defaultValue = "stdout,time,memory,stderr,token,compile_output,message,status") String fields //필드는 구현하지 않기로 함
    ){
        return ResponseEntity.ok(
                new GradeBatchResponse(
                    gradeQueryService.getGrades(tokens)
                        .stream().map(grade -> {
                            if (base64_encoded)
                                return GradeResponse.fromBase64Encoded(grade);
                            else
                                return GradeResponse.from(grade);
                        }).toList()));
    }

}
