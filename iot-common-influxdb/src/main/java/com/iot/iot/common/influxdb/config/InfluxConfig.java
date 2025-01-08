package com.iot.iot.common.influxdb.config;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.domain.IsOnboarding;
import com.influxdb.client.domain.OnboardingRequest;
import com.influxdb.client.domain.OnboardingResponse;
import com.influxdb.client.service.SetupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Response;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.Instant;

/**
 * @Author Orchid
 * @Create 2024/4/6
 * @Remark
 */
@Slf4j
@Configuration
@EnableConfigurationProperties( InfluxProperties.class )
public class InfluxConfig {

    @Resource
    InfluxProperties properties;

    /**
     * 返回influx http客户端,如果用代码的方式进行初始化,则务必查看日志保留token或设置密码
     *
     * @return InfluxDBClient
     * @throws IOException IOException
     */
    @Bean
    public InfluxDBClient influxDBClient() throws IOException {
        // todo 这里没有校验token和user同时为null的情况
        if ( properties.getToken() == null || properties.getToken().isEmpty() ) {
            // check step
            InfluxDBClient influxDBClient = InfluxDBClientFactory.create( properties.getUrl() );
            SetupService setupService = influxDBClient.getService( SetupService.class );
            String zapTraceSpan = Instant.now().toString();
            Response< IsOnboarding > response = setupService.getSetup( zapTraceSpan ).execute();
            if ( response.isSuccessful() && response.body() != null && response.body().getAllowed() ) {
                // 未初始化
                OnboardingRequest onboardingRequest = new OnboardingRequest();
                // 必选
                onboardingRequest.setUsername( properties.getUsername() );
                onboardingRequest.setOrg( properties.getOrganization() );
                onboardingRequest.setBucket( properties.getBucket() );
                // 可选 如果不设置密码则无法登录web ui
                onboardingRequest.setPassword( properties.getPassword() );
                Response< OnboardingResponse > onBoardingResponse = setupService.postSetup( onboardingRequest, zapTraceSpan ).execute();
                OnboardingResponse onboardingResponse = onBoardingResponse.body();
                log.info( "influx setup:token{}", onboardingResponse.getAuth().getToken() );
                return InfluxDBClientFactory.create( properties.getUrl(), onboardingResponse.getAuth().getToken().toCharArray(), properties.getOrganization(), properties.getBucket() );
            }
        }
        return InfluxDBClientFactory.create( properties.getUrl(), properties.getToken().toCharArray(), properties.getOrganization(), properties.getBucket() );
    }
}
