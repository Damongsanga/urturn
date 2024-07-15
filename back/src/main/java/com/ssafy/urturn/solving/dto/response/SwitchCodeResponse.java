package com.ssafy.urturn.solving.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SwitchCodeResponse {

    static SwitchCodeResponse empty = new SwitchCodeResponse(null, null, null, null, null);

    private final Boolean isFinalRound;
    private final Boolean isPair;
    private final Boolean isHost;
    private final Long memberId;
    private final Long pairId;

    public static SwitchCodeResponse empty(){
        return empty;
    }

    public boolean isEmpty() {
        return this.equals(empty);
    }

    public boolean isFinalRound() {
        return this.isFinalRound;
    }

    public boolean isPair() {
        return this.isPair;
    }

    public Boolean isHost() {
        return this.isHost;
    }

    public Long getMemberId() {
        return this.memberId;
    }

    public Long getPairId() {
        return this.pairId;
    }
}
