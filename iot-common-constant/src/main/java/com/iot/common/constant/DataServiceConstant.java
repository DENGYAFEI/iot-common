package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/18
 * @Remark 数据服务 相关常量
 */
public class DataServiceConstant {

    private DataServiceConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 服务名
     */
    public static final String SERVICE_NAME = "iot-data-service";

    public static final String VALUE_URL_PREFIX = "/data/point-value";

}
