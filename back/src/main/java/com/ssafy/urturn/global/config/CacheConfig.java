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

    private Map<String, RedisCacheConfiguration> customConfigurationMap() {
        Map<String, RedisCacheConfiguration> customConfigurationMap = new HashMap<>();
        customConfigurationMap.put(RedisCacheKey.ROOMIDCACHE, defaultConfiguration().entryTtl(Duration.ofMinutes(5L)));
        customConfigurationMap.put(RedisCacheKey.RECENTROOMID, defaultConfiguration().entryTtl(Duration.ofSeconds(5L)));
        customConfigurationMap.put(RedisCacheKey.ROOMINFODTOCACHE, defaultConfiguration().entryTtl(Duration.ofSeconds(5L)));
        customConfigurationMap.put(RedisCacheKey.ROUNDCODECACHE, defaultConfiguration().entryTtl(Duration.ofSeconds(5L)));
        return customConfigurationMap;
    }
}
