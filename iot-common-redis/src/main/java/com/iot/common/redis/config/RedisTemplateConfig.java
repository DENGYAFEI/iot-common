//package com.iot.common.redis.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.AllArgsConstructor;
//import org.springframework.boot.autoconfigure.AutoConfigureBefore;
//import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
///**
// * @Author Orchid
// * @Create 2024/4/2
// * @Remark RedisTemplate
// */
//@Configuration
//@AllArgsConstructor
//@AutoConfigureBefore( RedisAutoConfiguration.class )
//public class RedisTemplateConfig {
//
//    @Bean
//    public RedisTemplate<String,Object> redisTemplate( RedisConnectionFactory redisConnectionFactory){
//
//        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        //jackson序列化配置
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.setVisibility( PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        //objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        objectMapper.activateDefaultTyping( LaissezFaireSubTypeValidator.instance,ObjectMapper.DefaultTyping.NON_FINAL);
//
//        // 注册Java 8日期时间支持模块
//        objectMapper.registerModule(new JavaTimeModule());
//
//        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//
//        //string序列化配置
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//
//        //key采用string的序列化方式
//        redisTemplate.setKeySerializer(stringRedisSerializer);
//        //hash的key也采用String的序列化方式
//        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        //value采用jackson序列化方式
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//        //hash的value采用jackson的序列化方式
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//
//    //@Resource
//    //private RedisConnectionFactory factory;
//
//    //@Bean
//    //public RedisTemplate< String, Object > redisTemplate() {
//    //    RedisTemplate< String, Object > template = new RedisTemplate<>();
//    //    template.setConnectionFactory( factory );
//    //    // Json 序列化配置
//    //    Jackson2JsonRedisSerializer< Object > serializer = new Jackson2JsonRedisSerializer<>( Object.class );
//    //    serializer.setObjectMapper(
//    //            new ObjectMapper()
//    //                    .setVisibility( PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY )
//    //                    .activateDefaultTyping( LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL )
//    //    );
//    //    // 配置 Key & Value 序列化
//    //    template.setKeySerializer( new StringRedisSerializer() );
//    //    template.setValueSerializer( serializer );
//    //    template.setHashKeySerializer( new StringRedisSerializer() );
//    //    template.setHashValueSerializer( serializer );
//    //    return template;
//    //}
//
//}
