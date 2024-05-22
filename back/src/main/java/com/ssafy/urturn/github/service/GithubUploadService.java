package com.ssafy.urturn.github.service;

import static com.ssafy.urturn.global.exception.errorcode.CustomErrorCode.NO_MEMBER;

import com.ssafy.urturn.github.GithubUploadClient;
import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CommonErrorCode;
import com.ssafy.urturn.history.dto.HistoryRetroDto;
import com.ssafy.urturn.history.entity.History;
import com.ssafy.urturn.history.repository.HistoryRepository;
import com.ssafy.urturn.member.entity.Member;
import com.ssafy.urturn.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GithubUploadService {

    private final MemberRepository memberRepository;
    private final HistoryRepository historyRepository;
    private final GithubUploadClient githubUploadClient;


    public String upload(String githubUniqueId) {
        Member member = memberRepository.findByGithubUniqueId(githubUniqueId).orElseThrow(() -> new RestApiException(
            NO_MEMBER));

        HistoryRetroDto historyDto = historyRepository.getMostRecentHistoryByMemberId(member.getId());

        try{
            return githubUploadClient.uploadRetro(member, historyDto);
        } catch (RestApiException e){
            throw e;
        } catch (Exception e){
            throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR, "github 업로드 과정에서 문제가 발생하였습니다." + e.getMessage());
        }
    }
}
