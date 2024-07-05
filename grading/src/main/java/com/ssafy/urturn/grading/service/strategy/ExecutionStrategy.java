package com.ssafy.urturn.grading.service.strategy;

import com.ssafy.urturn.grading.domain.Grade;
import com.ssafy.urturn.grading.service.dto.TokenWithStatus;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface ExecutionStrategy {
    CompletableFuture<TokenWithStatus> execute(Grade grade);
}
