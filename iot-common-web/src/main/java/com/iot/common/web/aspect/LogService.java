package com.iot.common.web.aspect;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.iot.common.web.filter.RequestWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

@Slf4j
@Component
public class LogService {

    public static String getParameters( HttpServletRequest request ) {
        StringBuilder params = new StringBuilder();
        if ( HttpMethod.POST.name().equals( request.getMethod() ) ) {
            params
                    .append( "body:[" )
                    .append( ( ( RequestWrapper ) request ).getBody() )
                    .append( "]" );
        }
        Map< String, String[] > parameterMap = request.getParameterMap();
        Object[] keys = parameterMap.keySet().toArray();
        if ( keys.length > 0 ) {
            params.append( " params:{" );
            for ( int i = 0; i < keys.length; i++ ) {
                params
                        .append( i == 0 ? "" : "," )
                        .append( keys[i].toString() )
                        .append( ":" )
                        .append( Arrays.toString( parameterMap.get( keys[i].toString() ) ) );
            }
            params.append( "}" );
        }
        return params.toString();
    }

    public static String getStackTraceString( Exception e ) {
        StringBuilder str = new StringBuilder( "系统异常\n" );
        str.append( e );
        str.append( getStackTraceStr( e.getStackTrace() ) );
        str.append( getCauseStr( e ) );
        return str.toString();
    }

    private static String getStackTraceStr( StackTraceElement[] stackTraceElements ) {
        StringBuilder str = new StringBuilder();
        str.append( "\n" );
        for ( StackTraceElement stackTraceElement : stackTraceElements ) {
            str.append( "\tat " )
                    .append( stackTraceElement.getClassName() )
                    .append( "." )
                    .append( stackTraceElement.getMethodName() )
                    .append( "(" )
                    .append( stackTraceElement.getFileName() )
                    .append( ":" )
                    .append( stackTraceElement.getLineNumber() )
                    .append( ")\n" );
        }
        return str.toString();
    }

    private static String getCauseStr( Throwable e ) {
        StringBuilder str = new StringBuilder();
        if ( ObjectUtil.isNotNull( e.getCause() ) ) {
            str.append( "Caused by:" )
                    .append( e.getCause() )
                    .append( getStackTraceStr( e.getCause().getStackTrace() ) );
        }
        return str.toString();
    }

    public static String getClientIP( HttpServletRequest request ) {
        String ip = request.getHeader( "X-Real-IP" );
        if ( StringUtils.isBlank( ip ) ) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
