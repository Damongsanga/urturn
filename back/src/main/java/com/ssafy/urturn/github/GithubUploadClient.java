package com.ssafy.urturn.github;

import com.ssafy.urturn.github.dto.GithubUploadRequestDto;
import com.ssafy.urturn.global.auth.dto.GithubOAuthMemberInfoResponse;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.member.entity.Member;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
@RequiredArgsConstructor
public class GithubUploadClient {
    private static final RestTemplate restTemplate = new RestTemplate();

    private final GithubUploadUtil githubUtil;
    static final String GITHUB_API_URL = "https://api.github.com/repos/";

    public String uploadHistory(Member member, History history){
        log.info("url : {}", githubUtil.makeURl(member));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(member.getGithubAccessToken());

        HttpEntity<GithubUploadRequestDto> entity = new HttpEntity<>(
            new GithubUploadRequestDto(githubUtil.makeMessage(), member.getNickname(), member.getEmail(), githubUtil.makeContent(history)),
            headers
        );

        return restTemplate.exchange(
            githubUtil.makeURl(member),
            HttpMethod.PUT,
            entity,
            String.class)
            .getBody();

    }

}
