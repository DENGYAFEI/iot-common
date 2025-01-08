//package com.iot.common.rabbitmq.config;
//
//import com.iot.common.constant.RabbitConstant;
//import org.springframework.amqp.core.TopicExchange;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
///**
// * @Author Orchid
// * @Create 2024/4/2
// * @Remark
// */
//@Configuration
//public class ExchangeConfig {
//
//    /**
//     * 驱动同步相关，平台端、驱动端可负载
//     */
//    @Bean
//    TopicExchange syncExchange() {
//        return new TopicExchange( RabbitConstant.TOPIC_EXCHANGE_SYNC, true, false );
//    }
//
//    /**
//     * 事件相关，平台端可负载
//     */
//    @Bean
//    TopicExchange eventExchange() {
//        return new TopicExchange( RabbitConstant.TOPIC_EXCHANGE_EVENT, true, false );
//    }
//
//    /**
//     * 元数据相关，平台端广播，驱动端订阅
//     */
//    @Bean
//    TopicExchange metadataExchange() {
//        return new TopicExchange( RabbitConstant.TOPIC_EXCHANGE_METADATA, true, false );
//    }
//
//    /**
//     * 指令相关，驱动端可负载
//     */
//    @Bean
//    TopicExchange commandExchange() {
//        return new TopicExchange( RabbitConstant.TOPIC_EXCHANGE_COMMAND, true, false );
//    }
//
//    /**
//     * 数据相关，平台端可负载
//     */
//    @Bean
//    TopicExchange valueExchange() {
//        return new TopicExchange( RabbitConstant.TOPIC_EXCHANGE_VALUE, true, false );
//    }
//
//    /**
//     * MQTT 相关，平台端可负载
//     */
//    @Bean
//    TopicExchange mqttExchange() {
//        return new TopicExchange( RabbitConstant.TOPIC_EXCHANGE_MQTT, true, false );
//    }
//
//}
