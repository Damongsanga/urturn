package com.ssafy.urturn.solving.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

public record SubmitCodeResponse(
        Boolean isAccepted,
        Boolean isHost,
        Boolean isPair,
        Long managerId,
        Long pairId) {
}
