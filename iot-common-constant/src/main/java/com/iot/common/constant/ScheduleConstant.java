package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 任务调度 相关常量
 */
public class ScheduleConstant {

    private ScheduleConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 驱动任务调度分组
     */
    public static final String DRIVER_SCHEDULE_GROUP = "DriverScheduleGroup";

    /**
     * 读任务
     */
    public static final String READ_SCHEDULE_JOB = "ReadScheduleJob";

    /**
     * 自定义任务
     */
    public static final String CUSTOM_SCHEDULE_JOB = "CustomScheduleJob";

    /**
     * 状态任务
     */
    public static final String STATUS_SCHEDULE_JOB = "StatusScheduleJob";

    /**
     * 驱动状态任务 Corn
     */
    public static final String DRIVER_STATUS_CORN = "0/5 * * * * ?";

}
