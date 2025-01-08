package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/7
 * @Remark 管理服务 相关常量
 */
public class ManagerServiceConstant {

    private ManagerServiceConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * 服务名
     */
    public static final String SERVICE_NAME = "iot-manager-service";

    public static final String DRIVER_URL_PREFIX = "/manager/driver";
    public static final String DICT_URL_PREFIX = "/manager/dict";
    public static final String DICT_ITEM_URL_PREFIX = "/manager/dictItem";
    public static final String DRIVER_ATTRIBUTE_URL_PREFIX = "/manager/driver-attribute";
    public static final String POINT_ATTRIBUTE_URL_PREFIX = "/manager/point-attribute";
    public static final String PROFILE_URL_PREFIX = "/manager/profile";
    public static final String POINT_URL_PREFIX = "/manager/point";
    public static final String POINT_GENERAL_URL_PREFIX = "/manager/general/point";
    public static final String DEVICE_URL_PREFIX = "/manager/device";
    public static final String POINT_ATTRIBUTE_CONFIG_URL_PREFIX = "/manager/point-attribute-config";
    public static final String DRIVER_ATTRIBUTE_CONFIG_URL_PREFIX = "/manager/driver-attribute-config";

}
