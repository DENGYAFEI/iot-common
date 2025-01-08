//package com.iot.common.rabbitmq.config;
//
//import com.iot.common.base.utils.EnvironmentUtil;
//import com.iot.common.constant.EnvironmentConstant;
//import com.iot.common.constant.RabbitConstant;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.env.EnvironmentPostProcessor;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.core.env.ConfigurableEnvironment;
//
///**
// * @Author Orchid
// * @Create 2024/4/2
// * @Remark RabbitMQ 环境变量配置
// */
//@Slf4j
//@Configuration
//@Order( Ordered.LOWEST_PRECEDENCE - 100 )
//public class RabbitmqEnvironmentConfig implements EnvironmentPostProcessor {
//
//    @Override
//    public void postProcessEnvironment( ConfigurableEnvironment environment, SpringApplication application ) {
//        // 此处配置用于开发环境下多人开发，通过 env 和 group 标识区分不同对 exchange，queue，topic
//        String env = environment.getProperty( EnvironmentConstant.SPRING_ENV, String.class );
//        String group = environment.getProperty( EnvironmentConstant.SPRING_GROUP, String.class );
//
//        String tag = EnvironmentUtil.getTag( env, group );
//
//        // Sync
//        RabbitConstant.TOPIC_EXCHANGE_SYNC = tag + RabbitConstant.TOPIC_EXCHANGE_SYNC;
//        RabbitConstant.QUEUE_SYNC_UP = tag + RabbitConstant.QUEUE_SYNC_UP;
//        RabbitConstant.QUEUE_SYNC_DOWN_PREFIX = tag + RabbitConstant.QUEUE_SYNC_DOWN_PREFIX;
//
//        // Event
//        RabbitConstant.TOPIC_EXCHANGE_EVENT = tag + RabbitConstant.TOPIC_EXCHANGE_EVENT;
//        RabbitConstant.QUEUE_DRIVER_EVENT = tag + RabbitConstant.QUEUE_DRIVER_EVENT;
//        RabbitConstant.QUEUE_DEVICE_EVENT = tag + RabbitConstant.QUEUE_DEVICE_EVENT;
//
//        // Metadata
//        RabbitConstant.TOPIC_EXCHANGE_METADATA = tag + RabbitConstant.TOPIC_EXCHANGE_METADATA;
//        RabbitConstant.QUEUE_DRIVER_METADATA_PREFIX = tag + RabbitConstant.QUEUE_DRIVER_METADATA_PREFIX;
//
//        // Command
//        RabbitConstant.TOPIC_EXCHANGE_COMMAND = tag + RabbitConstant.TOPIC_EXCHANGE_COMMAND;
//        RabbitConstant.QUEUE_DRIVER_COMMAND_PREFIX = tag + RabbitConstant.QUEUE_DRIVER_COMMAND_PREFIX;
//        RabbitConstant.QUEUE_DEVICE_COMMAND_PREFIX = tag + RabbitConstant.QUEUE_DEVICE_COMMAND_PREFIX;
//
//        // Point Value
//        RabbitConstant.TOPIC_EXCHANGE_VALUE = tag + RabbitConstant.TOPIC_EXCHANGE_VALUE;
//        RabbitConstant.QUEUE_POINT_VALUE = tag + RabbitConstant.QUEUE_POINT_VALUE;
//    }
//
//}
