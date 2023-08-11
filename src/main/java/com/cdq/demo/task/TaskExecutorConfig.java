package com.cdq.demo.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
class TaskExecutorConfig {

    @Bean
    public ThreadPoolTaskExecutor configureThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // Number of threads to keep in the pool
        executor.setMaxPoolSize(10); // Maximum number of threads in the pool
        executor.setQueueCapacity(25); // Size of the queue to hold tasks waiting for execution
        executor.setThreadNamePrefix("TaskExecutor-");
        executor.initialize();
        return executor;
    }
}
