package com.ssafy.urturn.github;

import com.ssafy.urturn.github.service.GithubUploadService;
import com.ssafy.urturn.global.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/github")
@RequiredArgsConstructor
@Slf4j
public class GithubApiController {

    private final GithubUploadService githubUploadService;
    private final OAuthService oAuthService;

    @GetMapping("/upload")
    public ResponseEntity<?> refreshAccessToken(@RequestParam String code){
        log.info("code : {}", code);
        String githubUniqueId = oAuthService.refreshAccessToken(code);
        githubUploadService.upload(githubUniqueId);
        return ResponseEntity.ok().build();
    }

}
