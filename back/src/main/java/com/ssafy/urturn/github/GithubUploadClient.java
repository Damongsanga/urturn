package com.ssafy.urturn.github;

import com.ssafy.urturn.github.dto.GithubUploadRequestDto;
import com.ssafy.urturn.global.auth.dto.GithubOAuthMemberInfoResponse;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.member.entity.Member;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class GithubUploadClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    private static final String GITHUB_API_URL = "https://api.github.com/repos/";

    public String uploadHistory(Member member, History history){
        log.info("url : {}",makeURl(member));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(member.getGithubAccessToken());

        HttpEntity<GithubUploadRequestDto> entity = new HttpEntity<>(
            new GithubUploadRequestDto(makeMessage(), member.getNickname(), member.getEmail(), makeContent(history)),
            headers
        );

        return restTemplate.exchange(
            makeURl(member),
            HttpMethod.PUT,
            entity,
            String.class)
            .getBody();

    }

    private String makeContent(History history){
        return "bXkgbmV3IGZpbGUgY29udGVudHM=";
    }

    private String makeMessage(){
        return "test message";
    }

    // 매우 임시로 만든 URL
    private String makeURl(Member member){
        return GITHUB_API_URL + member.getNickname() + "/" + member.getRepository() + "/contents/" + LocalDateTime.now() + ".md";
    }

}
