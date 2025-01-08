package com.iot.common.thread.entity.property;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * @Author Orchid
 * @Create 2024/9/23
 * @Remark 优先级任务
 */
@NoArgsConstructor
@AllArgsConstructor
public class PriorityTask implements Runnable, Priority {

    private long priority;

    private Runnable runnable;

    @Override
    public long getPriority() {
        return priority;
    }

    @Override
    public void run() {
        runnable.run();
    }

    public static PriorityTask create(long priority, Runnable runnable) {
        return new PriorityTask(priority, runnable);
    }

}
