package com.ssafy.urturn.history.dto;

import com.ssafy.urturn.history.HistoryResult;
import com.ssafy.urturn.problem.Language;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRetroDto {

    private Long historyId;
    private String managerNickname;
    private String pairNickname;
    private String title1;
    private String title2;
    private String content1;
    private String content2;
    private String code1;
    private String code2;
    private Language language1;
    private Language language2;
    private String retro1;
    private String retro2;
    private int totalRound;
    private HistoryResult result;
    private LocalDateTime endTime;

}
