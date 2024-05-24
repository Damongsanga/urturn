package com.ssafy.urturn.global.cache;

import org.springframework.stereotype.Component;

@Component
public class RedisCacheKey {
    public static final String ROOMIDCACHE = "roomIdCache";
    public static final String RECENTROOMID = "recentRoomId";
    public static final String ROOMINFODTOCACHE = "roomInfoDtoCache";
    public static final String ROUNDCODECACHE = "roundCodeCache";
}
