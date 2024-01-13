package com.edwinkam.blackjack.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import java.util.concurrent.Executor;
@Configuration
public class TaskExecutorConfig {
    private int simulatorMaxThreads = 8;
    @Bean("blackjackExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("BlackJack-");
        executor.initialize();
        return executor;
    }

    @Bean("simulatorExecutor")
    public Executor simulatorExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(simulatorMaxThreads);
        executor.setMaxPoolSize(simulatorMaxThreads);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("simulatorExecutor-");
        executor.initialize();
        return executor;
    }
}