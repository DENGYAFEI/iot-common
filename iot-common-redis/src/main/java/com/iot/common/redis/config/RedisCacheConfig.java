//package com.iot.common.redis.config;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import lombok.Setter;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import javax.annotation.Resource;
//import java.time.Duration;
//
///**
// * @Author Orchid
// * @Create 2024/4/2
// * @Remark Redis Cache
// */
////@Configuration
////@ConfigurationProperties( prefix = "spring.cache.redis" )
//public class RedisCacheConfig extends CachingConfigurerSupport {
//
//    @Resource
//    private RedisConnectionFactory factory;
//
//    @Setter
//    private Duration timeToLive;
//
//    /**
//     * 自定义缓存 Key 生成策略
//     */
//    @Bean
//    public KeyGenerator firstKeyGenerator() {
//        return ( target, method, params ) -> params[0].toString();
//    }
//
//    /**
//     * 自定义缓存 Key 生成策略
//     */
//    @Bean
//    public KeyGenerator commonKeyGenerator() {
//        final String dot = ".";
//        final String hashTag = "#";
//        return ( target, method, params ) -> {
//            StringBuilder sb = new StringBuilder();
//            sb.append( target.getClass().getName() );
//            sb.append( dot );
//            sb.append( method.getName() );
//            sb.append( hashTag );
//            for ( Object obj : params ) {
//                sb.append( obj.toString() );
//            }
//            return sb.toString();
//        };
//    }
//
//    /**
//     * 自定义 RedisCacheManager 类，主要是设置序列化，解决乱码问题
//     */
//    @Bean
//    @Override
//    public CacheManager cacheManager() {
//        RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.builder( factory );
//
//        // Json 序列化配置
//        Jackson2JsonRedisSerializer< Object > serializer = new Jackson2JsonRedisSerializer<>( Object.class );
//        serializer.setObjectMapper( new ObjectMapper()
//                .setVisibility( PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY )
//                .activateDefaultTyping( LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL ) );
//
//        // 配置 Key & Value 序列化
//        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
//                .serializeKeysWith( RedisSerializationContext.SerializationPair.fromSerializer( new StringRedisSerializer() ) )
//                .serializeValuesWith( RedisSerializationContext.SerializationPair.fromSerializer( serializer ) )
//                .disableCachingNullValues().entryTtl( timeToLive );
//        return builder.cacheDefaults( config ).build();
//    }
//
//}
