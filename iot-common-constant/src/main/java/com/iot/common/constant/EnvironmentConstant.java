package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark
 */
public class EnvironmentConstant {

    private EnvironmentConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 开发环境
     */
    public static final String ENV_DEV = "dev";

    /**
     * 测试环境
     */
    public static final String ENV_TEST = "test";

    /**
     * 预发布环境
     */
    public static final String ENV_PRE = "pre";

    /**
     * 生产环境
     */
    public static final String ENV_PRO = "pro";

    /**
     * 服务是否开启 Https
     */
    public static final String EUREKA_TLS_ENABLE = "eureka.client.tls.enabled";

    /**
     * Eureka 服务注册中心 Url
     */
    public static final String EUREKA_SERVICE_URL = "eureka.client.service-url.defaultZone";

    /**
     * 当前环境类型
     */
    public static final String SPRING_ENV = "spring.env";

    /**
     * 当前分组
     */
    public static final String SPRING_GROUP = "spring.group";

    /**
     * 应用名称
     */
    public static final String SPRING_APPLICATION_NAME = "spring.application.name";

    /**
     * 驱动租户名称
     */
    public static final String DRIVER_TENANT = "driver.tenant";

    /**
     * 驱动节点
     */
    public static final String DRIVER_NODE = "driver.node";

    /**
     * 驱动服务
     */
    public static final String DRIVER_SERVICE = "driver.service";

    /**
     * 驱动主机
     */
    public static final String DRIVER_HOST = "driver.host";

    /**
     * 驱动客户端名称
     */
    public static final String DRIVER_CLIENT = "driver.client";

    /**
     * 驱动端口
     */
    public static final String DRIVER_PORT = "driver.port";

    /**
     * 驱动 Mqtt 客户端名称
     */
    public static final String MQTT_CLIENT = "driver.mqtt.client";

    /**
     * 驱动 Mqtt Topic 前缀
     */
    public static final String MQTT_PREFIX = "driver.mqtt.topic-prefix";

}
