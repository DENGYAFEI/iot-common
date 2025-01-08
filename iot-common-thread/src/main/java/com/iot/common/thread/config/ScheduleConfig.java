package com.iot.common.thread.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class ScheduleConfig {

    @Bean
    public TaskScheduler taskScheduler() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize( corePoolSize );
        threadPoolTaskScheduler.setThreadNamePrefix( "schedule-" );
        threadPoolTaskScheduler.setRemoveOnCancelPolicy( true );
        return threadPoolTaskScheduler;
    }
}
