package com.ssafy.urturn.global;

import static com.ssafy.urturn.global.exception.errorcode.CommonErrorCode.*;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestLockService {

    private final RedissonClient redissonClient;

    public String generateLockKey(RequestLockType requestLockType, Object... params) {
        return "REQUEST_LOCKS/" + requestLockType.name() + ":" + String.join("_#_", convertToStringArray(params));
    }

    public RLock getLock(String lockName) {
        return redissonClient.getLock(lockName);
    }

    private String[] convertToStringArray(Object[] params) {
        String[] stringParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            stringParams[i] = String.valueOf(params[i]);
        }
        return stringParams;
    }

}
