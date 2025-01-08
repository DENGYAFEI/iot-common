package com.iot.common.thread.entity.property;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 配置线程池属性
 */
@Setter
@Getter
public class ThreadProperty {

    /**
     * 线程名称前缀
     */
    private String prefix;

    /**
     * 核心池大小
     */
    private int corePoolSize;

    /**
     * 线程池最大线程数
     */
    private int maximumPoolSize;

    /**
     * 线程存活时间 | 对于空闲线程的等待时间
     */
    private int keepAliveTime;

}
