package com.ssafy.urturn.global.config;

import com.ssafy.urturn.global.cache.RedisCacheKey;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

// Redis를 기본 캐시 저장소로 설정
@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {
    private final RedisConnectionFactory redisConnectionFactory;
    @Bean
    public RedisCacheManager cacheManager() {
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfiguration())
                .withInitialCacheConfigurations(customConfigurationMap())
                .build();
    }

    private RedisCacheConfiguration defaultConfiguration(){
        return RedisCacheConfiguration.defaultCacheConfig()
            // 캐시의 기본 만료 시간 설정 (2시간)
            .entryTtl(Duration.ofHours(2L))
            // 키와 값의 직렬화 방식 설정
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }

    /**
     * [ROOMIDCACHE] 입장코드 : 방ID = 2시간. 2시간동안 페어가 구해지지 않으면 방 입장 불가.
     * [ROOMINFODTOCACHE] 방ID : 방 정보 = 6시간. ROOMIDCACHE + ROUNDCODECACHE 시간으로 방이 만들어지고 메인 플로우가 끝날 때까지의 예상 시간동안 유지
     * [RECENTROOMID] 유저ID : 방ID = 6시간. 방 정보와 동일.
     * [ROUNDCODECACHE] 방ID+문저ID : 라운드별 코드 = 4시간 (240분). 최대 라운드 모두 소요시 20 라운드 * 10분으로 200분 소요.
     * */
    private Map<String, RedisCacheConfiguration> customConfigurationMap() {
        Map<String, RedisCacheConfiguration> customConfigurationMap = new HashMap<>();
        customConfigurationMap.put(RedisCacheKey.ROOMIDCACHE, defaultConfiguration().entryTtl(Duration.ofHours(2L)));
        customConfigurationMap.put(RedisCacheKey.RECENTROOMID, defaultConfiguration().entryTtl(Duration.ofHours(6L)));
        customConfigurationMap.put(RedisCacheKey.ROOMINFODTOCACHE, defaultConfiguration().entryTtl(Duration.ofHours(6L)));
        customConfigurationMap.put(RedisCacheKey.ROUNDCODECACHE, defaultConfiguration().entryTtl(Duration.ofMinutes(4L)));
        return customConfigurationMap;
    }
}
