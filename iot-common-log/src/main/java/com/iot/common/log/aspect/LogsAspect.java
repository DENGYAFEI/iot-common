package com.iot.common.log.aspect;

import cn.hutool.extra.spring.SpringUtil;
import com.iot.common.log.annotation.Log;
import com.iot.common.log.factory.LogFactory;
import com.iot.common.log.pojo.dto.LogInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * @Author Orchid
 * @Create 2024/4/1
 * @Remark
 */
@Slf4j
@Aspect
@Component
public class LogsAspect {

    @Pointcut( "@annotation(com.iot.common.log.annotation.Log)" )
    public void logPoint() {
        // nothing to do
    }

    @Around( "logPoint() && @annotation(logAnnotation)" )
    public Object doAround( ProceedingJoinPoint joinPoint, Log logAnnotation ) throws Throwable {
        LocalDateTime start = LocalDateTime.now();
        Class< ? extends LogFactory > logFactoryClass = logAnnotation.logFactory();
        LogFactory logFactory = SpringUtil.getBean( logFactoryClass );
        Assert.notNull( logFactory, logFactoryClass.getName() + " Bean is not exist IOC" );
        //  代理对象class
        Class< ? > targetClass = AopUtils.getTargetClass( joinPoint.getTarget() );
        //  方法签名
        MethodSignature methodSignature = ( MethodSignature ) joinPoint.getSignature();
        //  实际参数
        Object[] args = joinPoint.getArgs();
        LogInfoDTO.LogInfoDTOBuilder builder = LogInfoDTO.builder();
        builder.logAnnotation( logAnnotation );
        builder.targetClass( targetClass );
        builder.methodSignature( methodSignature );
        builder.args( args );
        builder.start( start );
        //  执行对应方法
        try {
            Object proceed = joinPoint.proceed();
            builder.proceed( proceed );
            builder.end( LocalDateTime.now() );
            logFactory.createLog( builder.build() );
            return proceed;
        } catch ( Exception e ) {
            builder.whetherException( Boolean.TRUE );
            builder.exception( e );
            builder.end( LocalDateTime.now() );
            logFactory.createLog( builder.build() );
            throw e;
        }
    }
}
