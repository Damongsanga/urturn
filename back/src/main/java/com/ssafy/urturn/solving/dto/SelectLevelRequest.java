package com.ssafy.urturn.solving.dto;

import com.ssafy.urturn.member.Level;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class SelectLevelRequest {
    private static final Logger log = LoggerFactory.getLogger(SelectLevelRequest.class);
    private String roomId;
    private Level level;

    @Override
    public String toString() {
        return "SelectLevelRequest{" +
                "roomId='" + roomId + '\'' +
                ", level=" + level +
                '}';
    }
}
