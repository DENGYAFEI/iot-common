package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 事件 相关常量
 */
public class EventConstant {

    private EventConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * @Author Orchid
     * @Create 2024/4/3
     * @Remark 驱动事件 相关常量
     */
    public static class Driver {

        private Driver() {
            throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
        }

        /**
         * 驱动状态事件，该事件用于向 iot-manager-service 发送驱动的当前状态
         */
        public static final String STATUS = "driver_status";

        /**
         * 驱动注册事件，该事件用于向 iot-manager-service 注册驱动配置信息
         */
        public static final String REGISTER = "driver_register";
        public static final String REGISTER_BACK = "driver_register_back";

    }

    /**
     * @Author Orchid
     * @Create 2024/4/3
     * @Remark 设备事件 相关常量
     */
    public static class Device {

        private Device() {
            throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
        }

        /**
         * 设备状态事件
         */
        public static final String STATUS = "device_status";

        /**
         * 用于记录错误事件类型
         */
        public static final String ERROR = "device_error";

    }

}
