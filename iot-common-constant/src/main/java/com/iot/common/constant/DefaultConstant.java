package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 默认 相关常量
 */
public class DefaultConstant {

    private DefaultConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 默认ID：-1
     */
    public static final String DEFAULT_ID = "-1";

    /**
     * 默认数字：-1
     */
    public static final Integer DEFAULT_INT = -1;

    /**
     * 默认值：nil
     */
    public static final String DEFAULT_VALUE = "nil";

}
