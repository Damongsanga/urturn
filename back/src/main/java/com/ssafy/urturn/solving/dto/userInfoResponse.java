package com.ssafy.urturn.solving.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class userInfoResponse {
    private String myUserProfileUrl;
    private String myUserNickName;
    private String relativeUserProfileUrl;
    private String relativeUserNickName;
}
