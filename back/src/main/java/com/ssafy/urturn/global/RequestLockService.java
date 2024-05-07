package com.ssafy.urturn.global;

import static com.ssafy.urturn.global.exception.errorcode.CommonErrorCode.*;

import com.ssafy.urturn.global.exception.RestApiException;
import com.ssafy.urturn.global.exception.errorcode.CustomErrorCode;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RequestLockService {


    private final RedisTemplate<String, String> redisTemplate;

    public String generateLockKey(RequestLockType requestLockType, Object... params) {
        return "REQUEST_LOCKS/" + requestLockType.name() + "/" + String.join("_#_", convertToStringArray(params));
    }


    public void ifLockedThrowExceptionElseLock(String lockKey, Duration lockDuration) {
        try {
            Boolean isAbsent = redisTemplate.opsForValue().setIfAbsent(lockKey, "locked", lockDuration);
            if (isAbsent != null && !isAbsent) {
                throw new RestApiException(CustomErrorCode.REQUEST_LOCKED);
            }
        } catch (Exception ex) {
            throw new RestApiException(INTERNAL_SERVER_ERROR, "redis error");
        }
    }

    public void unlock(String key) {
        if (key == null) {
            return;
        }
        try {
            redisTemplate.delete(key);
        } catch (Exception ex) {
            throw new RestApiException(INTERNAL_SERVER_ERROR, "redis error");
        }
    }


    private String[] convertToStringArray(Object[] params) {
        String[] stringParams = new String[params.length];
        for (int i = 0; i < params.length; i++) {
            stringParams[i] = String.valueOf(params[i]);
        }
        return stringParams;
    }

}
