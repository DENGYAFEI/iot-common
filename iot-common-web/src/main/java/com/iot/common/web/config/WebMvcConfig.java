package com.iot.common.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark WebMvc配置
 */
// @Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    public static final String systemPath = System.getProperty( "user.dir" );

    @Bean
    CorsFilter getCorsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOriginPattern( "*" );
        corsConfiguration.addAllowedMethod( "*" );
        corsConfiguration.addAllowedHeader( "*" );
        corsConfiguration.addExposedHeader( "*" );
        source.registerCorsConfiguration( "/**", corsConfiguration );
        return new CorsFilter( source );
    }

    @Override
    public void addResourceHandlers( ResourceHandlerRegistry registry ) {
        //  org.webjars::swagger-ui
        registry.addResourceHandler( "/swagger-ui/**" )
                .addResourceLocations( "classpath:/META-INF/resources/webjars/swagger-ui/4.18.2/" );
        registry.addResourceHandler( "/upload/**" )
                .addResourceLocations( "file:" + systemPath + "/upload/" );
    }

}
