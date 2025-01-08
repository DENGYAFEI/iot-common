package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark Request Constant
 */
public class RequestConstant {

    private RequestConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 最大请求次数限制
     */
    public static final int DEFAULT_MAX_REQUEST_SIZE = 100;

    /**
     * @Author Orchid
     * @Create 2024/4/2
     * @Remark 自定义请求 Header 相关常量
     */
    public static class Header {

        private Header() {
            throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
        }

        /**
         * 自定义 租户 请求头
         */
        public static final String X_AUTH_TENANT = "X-Auth-Tenant";

        /**
         * 自定义 用户登陆名称 请求头
         */
        public static final String X_AUTH_LOGIN = "X-Auth-Login";

        /**
         * 自定义 Token 请求头
         */
        public static final String X_AUTH_TOKEN = "X-Auth-Token";

        /**
         * 自定义 租户ID 请求头
         */
        public static final String X_AUTH_TENANT_ID = "X-Auth-Tenant-Id";

        /**
         * 自定义 用户ID 请求头
         */
        public static final String X_AUTH_USER_ID = "X-Auth-User-Id";

        /**
         * 自定义 用户昵称 请求头
         */
        public static final String X_AUTH_NICK = "X-Auth-Nick";

        /**
         * 自定义 用户名称 请求头
         */
        public static final String X_AUTH_USER = "X-Auth-User";

    }

    /**
     * @Author Orchid
     * @Create 2024/4/2
     * @Remark 自定义请求 Message 相关常量
     */
    public static class Message {

        private Message() {
            throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
        }

        /**
         * 无效的权限请求头
         */
        public static final String INVALID_REQUEST = "Invalid request auth header";
    }

}
