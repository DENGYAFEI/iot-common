package com.iot.common.web.aspect;

import cn.hutool.core.date.DateUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author Orchid
 * @Create 2024/1/18
 * @Remark 日志A0P
 */
@Aspect
@Component
public class LoggerAspect {

    private final Logger logger = LoggerFactory.getLogger( getClass() );

    @Pointcut( value = "execution(* com.iot.*.controller..*(..))" )
    public void pointCut() {

    }

    @Around( value = "pointCut()" )
    public Object log( ProceedingJoinPoint joinPoint ) throws Throwable {
        HttpServletRequest request = ( ( ServletRequestAttributes ) Objects.requireNonNull( RequestContextHolder.getRequestAttributes() ) ).getRequest();
        Signature signature = joinPoint.getSignature();//(修饰符 包名 类名 方法名)
        Method method = ( ( MethodSignature ) signature ).getMethod();// 获取方法信息
        String requestTime = DateUtil.now();
        long startTime = System.currentTimeMillis();

        Object result = joinPoint.proceed();// 启动目标方法执行
        synchronized ( logger ) {
            logger.info( "***************************************************************************" );
            logger.info( "请求时间: " + requestTime );
            logger.info( "请求IP:  " + LogService.getClientIP( request ) );
            logger.info( "内容类型: " + request.getContentType() );
            logger.info( "请求方式: " + request.getMethod() );
            logger.info( "请求路径: " + request.getRequestURI() );
            logger.info( "请求接口: " + signature.getDeclaringType().getSimpleName() + "::" + method.getName() + "()" );
            logger.info( "请求参数: " + LogService.getParameters( request ) );
            logger.info( "执行耗时: " + ( System.currentTimeMillis() - startTime ) + "(ms)" );
            // logger.info("返回内容: " + result);
            logger.info( "***************************************************************************" );
        }
        return result;
    }

    @After( "@annotation(ex)" )
    public void exceptionLog( JoinPoint joinPoint, ExceptionHandler ex ) {
        HttpServletRequest request = ( ( ServletRequestAttributes ) Objects.requireNonNull( RequestContextHolder.getRequestAttributes() ) ).getRequest();
        Signature signature = joinPoint.getSignature();//(修饰符 包名 类名 方法名)
        MethodSignature methodSignature = ( MethodSignature ) signature;
        Method method = methodSignature.getMethod();// 获取方法信息
        synchronized ( logger ) {
            logger.info( "***************************************************************************" );
            logger.info( "请求IP:  " + request.getRemoteHost() );
            logger.info( "内容类型: " + request.getContentType() );
            logger.info( "请求方式: " + request.getMethod() );
            logger.info( "请求接口: " + signature.getDeclaringType().getSimpleName() + "::" + method.getName() + "()" );
            logger.info( "请求参数: " + LogService.getParameters( request ) );
            logger.info( "请求路径: " + request.getRequestURI() );
            Object[] args = joinPoint.getArgs();
            if ( args != null ) {
                for ( Object arg : args ) {
                    if ( arg instanceof Exception ) {
                        Exception e = ( Exception ) arg;
                        logger.error( LogService.getStackTraceString( e ) );
                    }
                }
            }
            logger.info( "***************************************************************************" );
        }
    }

}
