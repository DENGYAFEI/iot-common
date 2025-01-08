package com.iot.common.thread.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 自动注入application-thread.yml
 */
@Slf4j
@Configuration
@Order( Ordered.HIGHEST_PRECEDENCE )
public class ActiveThreadProfileConfig implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment( ConfigurableEnvironment environment, SpringApplication application ) {
        environment.addActiveProfile( "thread" );
    }

}
