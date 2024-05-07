package com.ssafy.urturn.solving.dto;

import com.ssafy.urturn.member.Level;
import lombok.Getter;

@Getter
public class SelectLevelRequest {
    private String roomId;
    private Level level;
}
