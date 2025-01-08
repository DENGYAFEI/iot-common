package com.iot.common.constant;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark MQ 常量
 */
public class RabbitConstant {

    private RabbitConstant() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    // Arguments
    public static final String MESSAGE_TTL = "x-message-ttl";
    public static final String AUTO_DELETE = "x-auto-delete";

    // Sync
    public static String TOPIC_EXCHANGE_SYNC = "iot.e.sync";
    public static final String ROUTING_SYNC_UP_PREFIX = "iot.r.sync.up.";
    public static String QUEUE_SYNC_UP = "iot.q.sync.up";
    public static final String ROUTING_SYNC_DOWN_PREFIX = "iot.r.sync.down.";
    public static String QUEUE_SYNC_DOWN_PREFIX = "iot.q.sync.down.";

    // Event
    public static String TOPIC_EXCHANGE_EVENT = "iot.e.event";
    public static final String ROUTING_DRIVER_EVENT_PREFIX = "iot.r.event.driver.";
    public static String QUEUE_DRIVER_EVENT = "iot.q.event.driver";
    public static final String ROUTING_DEVICE_EVENT_PREFIX = "iot.r.event.device.";
    public static String QUEUE_DEVICE_EVENT = "iot.q.event.device";

    // Metadata
    public static String TOPIC_EXCHANGE_METADATA = "iot.e.metadata";
    public static final String ROUTING_DRIVER_METADATA_PREFIX = "iot.r.metadata.driver.";
    public static String QUEUE_DRIVER_METADATA_PREFIX = "iot.q.metadata.driver.";

    // Command
    public static String TOPIC_EXCHANGE_COMMAND = "iot.e.command";
    public static final String ROUTING_DRIVER_COMMAND_PREFIX = "iot.r.command.driver.";
    public static String QUEUE_DRIVER_COMMAND_PREFIX = "iot.q.command.driver.";
    public static final String ROUTING_DEVICE_COMMAND_PREFIX = "iot.r.command.device.";
    public static String QUEUE_DEVICE_COMMAND_PREFIX = "iot.q.command.device.";

    // Value
    public static String TOPIC_EXCHANGE_VALUE = "iot.e.value";
    public static final String ROUTING_POINT_VALUE_PREFIX = "iot.r.value.point.";
    public static String QUEUE_POINT_VALUE = "iot.q.value.point";

    // Mqtt
    public static String TOPIC_EXCHANGE_MQTT = "iot.e.mqtt";
    public static final String ROUTING_MQTT_PREFIX = "iot.r.mqtt.";
    public static String QUEUE_MQTT = "iot.q.mqtt";

}
