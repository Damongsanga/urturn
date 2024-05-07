package com.ssafy.urturn.github;

import com.ssafy.urturn.github.service.GithubUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/github")
@RequiredArgsConstructor
public class TestGithubController {

    private final GithubUploadService githubUploadService;

    @PostMapping("/upload")
    public ResponseEntity<?> getResult(){
        return ResponseEntity.ok(githubUploadService.upload());
    }

}
