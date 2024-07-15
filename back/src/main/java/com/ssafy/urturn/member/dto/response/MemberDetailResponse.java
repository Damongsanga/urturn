package com.ssafy.urturn.member.dto.response;

import com.ssafy.urturn.member.Level;
import com.ssafy.urturn.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class MemberDetailResponse {
    private Long id;
    private String nickname;
    private String profileImage;
    private String repository;
    private int exp;
    private Level level;

    public static MemberDetailResponse makeResponse(Member member){
        return MemberDetailResponse.builder()
            .id(member.getId())
            .nickname(member.getNickname())
            .profileImage(member.getProfileImage())
            .repository(member.getRepository())
            .exp(member.getExp())
            .level(member.getLevel())
            .build();
    }
}
