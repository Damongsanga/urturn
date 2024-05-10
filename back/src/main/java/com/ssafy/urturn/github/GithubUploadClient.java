package com.ssafy.urturn.github;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.*;

import com.ssafy.urturn.github.dto.GithubUploadRequestDto;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.history.dto.HistoryRetroDto;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.member.entity.Member;
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

    public String uploadRetro(Member member, HistoryRetroDto historyDto){
        if (member.getRepository() == null) throw new RestApiException(NO_REPOSITORY);

        log.info("url : {}", githubUtil.makeURl(member));

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(member.getGithubAccessToken());

        HttpEntity<GithubUploadRequestDto> entity = new HttpEntity<>(
            new GithubUploadRequestDto(githubUtil.makeMessage(historyDto, member.getNickname()), member.getNickname(), member.getEmail(), githubUtil.makeContent(historyDto)),
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
