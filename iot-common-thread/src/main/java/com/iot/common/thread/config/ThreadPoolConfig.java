package com.iot.common.thread.config;

import com.iot.common.thread.entity.property.ThreadProperty;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 通用线程池配置
 */
@Slf4j
@Configuration
@ConfigurationProperties( prefix = "server" )
public class ThreadPoolConfig {

    @Setter
    private ThreadProperty thread;

    private final AtomicInteger threadPoolAtomic = new AtomicInteger( 1 );
    private final AtomicInteger scheduledThreadPoolAtomic = new AtomicInteger( 1 );

    /**
     * @Author Orchid
     * @Create 2024/4/2
     * @Remark 创建线程池
     */
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        return new ThreadPoolExecutor(
                thread.getCorePoolSize(),
                thread.getMaximumPoolSize(),
                thread.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(),
                r -> new Thread( r, "[T]" + thread.getPrefix() + threadPoolAtomic.getAndIncrement() ),
                ( r, e ) -> new BlockingRejectedExecutionHandler()
        );
    }

    /**
     * @Author Orchid
     * @Create 2024/4/2
     * @Remark 线程调度
     */
    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor() {
        return new ScheduledThreadPoolExecutor(
                thread.getCorePoolSize(),
                r -> new Thread( r, "[S]" + thread.getPrefix() + scheduledThreadPoolAtomic.getAndIncrement() ),
                ( r, e ) -> new BlockingRejectedExecutionHandler()
        );
    }

    /**
     * @Author Orchid
     * @Create 2024/4/2
     * @Remark 自定义拒绝策略
     */
    private static class BlockingRejectedExecutionHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution( Runnable runnable, ThreadPoolExecutor executor ) {
            try {
                log.info( "BlockingRejectedExecutionHandler: {}", executor.toString() );
                if ( !executor.isShutdown() ) {
                    runnable.run();
                }
            } catch ( Exception e ) {
                log.error( "BlockingRejectedExecutionHandler: {}", e.getMessage(), e );
            }
        }
    }

}
