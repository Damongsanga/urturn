package com.ssafy.urturn.room.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class UserInfoResponse {
    private String myUserProfileUrl;
    private String myUserNickName;

    @JsonProperty("relativeUserProfileUrl")
    private String pairProfileUrl;
    @JsonProperty("relativeUserNickName")
    private String pairNickName;


}
