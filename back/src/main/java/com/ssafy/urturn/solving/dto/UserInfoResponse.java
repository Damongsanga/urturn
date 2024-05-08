package com.ssafy.urturn.solving.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserInfoResponse {
    private String myUserProfileUrl;
    private String myUserNickName;

    @JsonProperty("relativeUserProfileUrl")
    private String pairProfileUrl;
    @JsonProperty("relativeUserNickName")
    private String pairNickName;
}
