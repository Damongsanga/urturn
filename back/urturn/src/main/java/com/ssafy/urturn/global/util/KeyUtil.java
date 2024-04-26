package com.ssafy.urturn.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class KeyUtil {
    public static String getRoomKey(Long roomId) {
        return "ROOMID:" + roomId;
    }

    public static String getCurrentMemberKey(String memberId) {
        return "CURRENT:" + memberId;
    }

    public static String getRefreshTokenKey(String memberId) {
        return "REFRESH_TOKEN:" + memberId;
    }
}