package com.ssafy.urturn.solving.cache;

import com.ssafy.urturn.solving.dto.roomInfoDto;
import com.ssafy.urturn.solving.dto.userCodeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;
import java.util.concurrent.locks.ReentrantLock;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class cacheDatas {

    private final ReentrantLock lock;

    private final RedisCacheManager redisCacheManager;

    public void clearAllCache(){

        redisCacheManager.getCacheNames().forEach(cacheName ->{
            redisCacheManager.getCache(cacheName).clear();
        });
    }

    // entrycode를 키로 하여 roomId를 roomIdCache라는 이름의 캐시에 저장.
    @CachePut(value = "roomIdCache", key="#entrycode")
    public String cacheRoomId(String entrycode, String roomId) {
        return roomId;
    }

    @Cacheable(value ="roomIdCache", key="#entrycode")
    public String cacheRoomId(String entrycode) {
        return null;
    }

    @CachePut(value = "roomInfoDtoCache", key="#roomId")
    public roomInfoDto cacheroomInfoDto(String roomId, roomInfoDto roomInfoDto){
        return roomInfoDto;
    }

    @Cacheable(value = "roomInfoDtoCache", key="#roomId")
    public roomInfoDto cacheroomInfoDto(String roomId){
        return null;
    }

    @Cacheable(value = "responseCache", key = "#roomId + '_' + #questionId")
    public List<userCodeDto> cacheCodes(String roomId, String questionId) {
        // 실제 캐시 저장소에서 데이터를 가져오는 로직은 필요하지 않음. 캐시 미스 시 null 반환.
        return null;
    }

    @CachePut(value = "responseCache", key = "#roomId + '_' + #questionId")
    public List<userCodeDto> updateCodeCache(String roomId, String questionId, userCodeDto newCode) {
        lock.lock();
        try{
            List<userCodeDto> currentCodes = cacheCodes(roomId, questionId);
            if (currentCodes == null) {
                currentCodes = new ArrayList<>();
            }
            currentCodes.add(newCode);
            return currentCodes;
        }finally {
            lock.unlock();
        }

    }

}
