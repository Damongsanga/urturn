package com.ssafy.urturn.global.auth.repository;

import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtRedisRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public void save(String refreshTokenKey, String refreshToken, long refreshTokenValidityInSeconds){
        redisTemplate.opsForValue().set(
                refreshTokenKey,
                refreshToken,
                refreshTokenValidityInSeconds,
                TimeUnit.SECONDS
        );
    }

    public void delete(String refreshTokenKey){
        redisTemplate.delete(refreshTokenKey);
    }

    public String find(String refreshTokenKey){
        return redisTemplate.opsForValue().get(refreshTokenKey);
    }
}
