package com.ssafy.urturn.room.dto;

import com.ssafy.urturn.member.Level;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class MemberIdDto {
    private static final Logger log = LoggerFactory.getLogger(MemberIdDto.class);

    private Long memberId;
    private Level level;

    @Override
    public String toString() {
        return "MemberIdDto{" +
                "memberId=" + memberId +
                ", level=" + level +
                '}';
    }
}
