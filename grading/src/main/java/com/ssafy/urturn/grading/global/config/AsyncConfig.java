package com.ssafy.urturn.grading.global.config;

import com.ssafy.urturn.grading.global.exception.GlobalAsyncExceptionHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    @Bean
    @ConditionalOnProperty(name="async.executor", havingValue = "prod", matchIfMissing = true)
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(5);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("async-executor-채점-");
        executor.setRejectedExecutionHandler((r, exec) -> {
            throw new IllegalArgumentException("비동기 요청을 더 이상 처리할 수 없습니다.");
        });
        executor.initialize();
        return executor;
    }

    @Bean
    @ConditionalOnProperty(name="async.executor", havingValue = "test")
    public Executor getAsyncExecutorForTest() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(4);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("async-executor-채점-");
        executor.setRejectedExecutionHandler((r, exec) -> {
            throw new IllegalArgumentException("비동기 요청을 더 이상 처리할 수 없습니다.");
        });
        executor.initialize();
        return executor;
    }

    // 람다로 깔끔하게 바꿀수도 있음!
//    @Bean
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return (ex, method, params) -> log.error("Method name : {}, param Count : {}\n\nException Cause -{}", method.getName(), params.length, ex.getMessage());
//    }

    @Bean
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new GlobalAsyncExceptionHandler();
    }
}