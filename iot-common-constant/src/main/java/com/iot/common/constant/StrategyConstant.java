package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/18
 * @Remark 策略工厂 相关常量
 */
public class StrategyConstant {

    private StrategyConstant() {
        throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
    }

    /**
     * @Author Orchid
     * @Create 2024/4/18
     * @Remark 存储相关的策略工厂 相关常量
     */
    public static class Storage {

        private Storage() {
            throw new IllegalStateException(ExceptionConstant.UTILITY_CLASS);
        }

        public static final String REPOSITORY_PREFIX = "storage" + SymbolConstant.DOUBLE_COLON;

        public static final String MYSQL = "mysql";
        public static final String MONGO = "mongo";

    }

}
