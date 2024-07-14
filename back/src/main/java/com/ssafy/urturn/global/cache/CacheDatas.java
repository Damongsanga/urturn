package com.ssafy.urturn.global.cache;

import com.ssafy.urturn.room.dto.RoomInfoDto;
import com.ssafy.urturn.solving.dto.UserCodeDto;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CacheDatas {

    private final RedisCacheManager redisCacheManager;

    public void clearAllCache(){
        redisCacheManager.getCacheNames().forEach(cacheName ->{
            Objects.requireNonNull(redisCacheManager.getCache(cacheName)).clear();
        });
    }

    // entrycode를 키로 하여 roomId를 roomIdCache라는 이름의 캐시에 저장.
    @CachePut(value = RedisCacheKey.ROOMIDCACHE, key="#entrycode")
    public String putRoomIdCache(String entrycode, String roomId) {
        return roomId;
    }

    @Cacheable(value =RedisCacheKey.ROOMIDCACHE, key="#entrycode")
    public String getRoomIdCache(String entrycode) {
        return null;
    }


    @CachePut(value = RedisCacheKey.RECENTROOMID, key="#memberId")
    public String putRecentRoomId(String memberId, String roomId){
        return roomId;
    };

    @Cacheable(value = RedisCacheKey.RECENTROOMID, key="#memberId")
    public String getRecentRoomId(String memberId){
        return null;
    };


    @CachePut(value = RedisCacheKey.ROOMINFODTOCACHE, key="#roomId")
    public RoomInfoDto putRoomInfo(String roomId, RoomInfoDto roomInfoDto){
        return roomInfoDto;
    }

    @Cacheable(value = RedisCacheKey.ROOMINFODTOCACHE, key="#roomId")
    public RoomInfoDto getRoomInfo(String roomId){
        return null;
    }

    @CacheEvict(value = RedisCacheKey.ROOMINFODTOCACHE, key="#roomId")
    public void deleteRoomInfo(String roomId){
        // delete cache
    }


    @CachePut(value = RedisCacheKey.ROUNDCODECACHE, key = "#roomId + '_' + #questionId")
    public List<UserCodeDto> putCacheCodes(String roomId, String questionId, List<UserCodeDto> list){
        return list;
    }

    @Cacheable(value = RedisCacheKey.ROUNDCODECACHE, key = "#roomId + '_' + #questionId")
    public List<UserCodeDto> getCacheCodes(String roomId, String questionId) {
        return new ArrayList<>();
    }

    @CacheEvict(value = RedisCacheKey.ROUNDCODECACHE, key = "#roomId + '_' + #questionId")
    public void deleteCacheCodes(String roomId, String questionId) {
        // delete cache
    }


}
