//package com.iot.common.rabbitmq.config;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.AcknowledgeMode;
//import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.connection.ConnectionFactory;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
//import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
//import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
//import org.springframework.amqp.support.converter.MessageConverter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//
///**
// * @Author Orchid
// * @Create 2024/4/2
// * @Remark
// */
//@Slf4j
//@Configuration
//public class RabbitConfig {
//
//    @Resource
//    private ConnectionFactory connectionFactory;
//
//    @Bean
//    public Jackson2JsonMessageConverter jsonMessageConverter() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        // 注册LocalDateTime的序列化器
//        JavaTimeModule javaTimeModule = new JavaTimeModule();
//        javaTimeModule.addSerializer( LocalDateTime.class, new LocalDateTimeSerializer( DateTimeFormatter.ISO_LOCAL_DATE_TIME ) );
//        objectMapper.registerModule( javaTimeModule );
//        // 注册LocalDateTime的反序列化器
//        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
//        objectMapper.disable( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS );
//        return new Jackson2JsonMessageConverter( objectMapper );
//    }
//
//    @Bean
//    RabbitTemplate rabbitTemplate( MessageConverter messageConverter ) {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate( connectionFactory );
//        rabbitTemplate.setMessageConverter( messageConverter );
//        rabbitTemplate.setMandatory( true );
//        rabbitTemplate.setReturnsCallback( message -> log.error( "Send message({}) to exchange({}), routingKey({}) failed: {}", message.getMessage(), message.getExchange(), message.getRoutingKey(), message.getReplyText() ) );
//        rabbitTemplate.setConfirmCallback( ( correlationData, ack, cause ) -> {
//            if ( !ack ) {
//                log.error( "CorrelationData({}) ack failed: {}", correlationData, cause );
//            }
//        } );
//        return rabbitTemplate;
//    }
//
//    @Bean
//    public RabbitListenerContainerFactory< SimpleMessageListenerContainer > rabbitListenerContainerFactory( MessageConverter messageConverter ) {
//        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
//        factory.setConnectionFactory( connectionFactory );
//        factory.setMessageConverter( messageConverter );
//        factory.setAcknowledgeMode( AcknowledgeMode.MANUAL );
//        return factory;
//    }
//}
