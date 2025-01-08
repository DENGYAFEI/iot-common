package com.iot.common.log.factory;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.iot.common.log.annotation.Log;
import com.iot.common.log.interfaces.LogFunction;
import com.iot.common.log.pojo.dto.LogInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author Orchid
 * @Create 2024/4/1
 * @Remark 默认Log日志工厂 不负责持久化(save) 只打印
 */
@Slf4j
@Component
public class DefaultLogFactory implements LogFactory {

    private static final Map< String, Logger > LOGGER_CACHE = new ConcurrentHashMap<>();

    @Override
    public void createLog( LogInfoDTO dto ) {
        //  出现异常的时候无视等级
        if ( Boolean.TRUE.equals( dto.getWhetherException() ) ) {
            log.error( getLogErrorMessage( dto ) );
            return;
        }
        LogFunction logFunction = getLogFunction( dto.getLogAnnotation(), dto.getTargetClass() );
        if ( ObjectUtil.isNull( logFunction ) ) {
            return;
        }
        logFunction.log( getLogMessage( dto ) );
    }

    private LogFunction getLogFunction( Log logAnnotation, Class< ? > targetClass ) {
        Logger logger = LOGGER_CACHE.computeIfAbsent( targetClass.getName(), key -> LoggerFactory.getLogger( targetClass ) );
        switch ( logAnnotation.level() ) {
            case INFO:
                return logger.isInfoEnabled() ? null : logger::info;
            case WARN:
                return logger.isWarnEnabled() ? null : logger::warn;
            case DEBUG:
                return logger.isDebugEnabled() ? null : logger::debug;
            case ERROR:
                return logger.isErrorEnabled() ? null : logger::error;
            default:
                throw new IllegalArgumentException( logAnnotation.level().toString() );
        }
    }

    private String getLogMessage( LogInfoDTO dto ) {
        StringJoiner joiner = new StringJoiner( "\n" );
        joiner.add( "***************************************************************************" );
        joiner.add( "触发时间: " + LocalDateTimeUtil.formatNormal( dto.getStart() ) );
        joiner.add( "实际参数: " + Arrays.toString( dto.getArgs() ) );
        joiner.add( "执行耗时: " + ( dto.getEnd().toInstant( ZoneOffset.UTC ).toEpochMilli() - dto.getStart().toInstant( ZoneOffset.UTC ).toEpochMilli() ) + "(ms)" );
        // joiner.add( "返回内容: " + dto.getProceed() );
        joiner.add( "***************************************************************************" );
        return joiner.toString();
    }

    private String getLogErrorMessage( LogInfoDTO dto ) {
        StringJoiner joiner = new StringJoiner( "\n" );
        joiner.add( "***************************************************************************" );
        joiner.add( "触发时间: " + LocalDateTimeUtil.formatNormal( dto.getStart() ) );
        joiner.add( "实际参数: " + Arrays.toString( dto.getArgs() ) );
        joiner.add( "执行耗时: " + ( dto.getEnd().toInstant( ZoneOffset.UTC ).toEpochMilli() - dto.getStart().toInstant( ZoneOffset.UTC ).toEpochMilli() ) + "(ms)" );
        joiner.add( "异常信息: " + dto.getException().getMessage() );
        joiner.add( "***************************************************************************" );
        return joiner.toString();
    }

}
