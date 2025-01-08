package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 异常 常量
 */
public class ExceptionConstant {

    private ExceptionConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 公共类实例化错误提示
     */
    public static final String UTILITY_CLASS = "Utility class";

    /**
     * 没有可用的服务
     */
    public static final String NO_AVAILABLE_SERVER = "No available server for client";

}
