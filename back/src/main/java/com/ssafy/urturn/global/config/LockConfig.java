package com.ssafy.urturn.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class LockConfig {
    @Bean
    public ReentrantLock reentrantLock() {
        return new ReentrantLock();
    }
}