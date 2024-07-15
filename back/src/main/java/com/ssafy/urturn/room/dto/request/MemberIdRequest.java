package com.ssafy.urturn.room.dto.request;

import com.ssafy.urturn.member.Level;
import lombok.Getter;

@Getter
public class MemberIdRequest {

    private Long memberId;
    private Level level;

}
