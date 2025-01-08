package com.iot.common.web.config;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.iot.common.exception.JsonException;
import com.iot.common.exception.NotFoundException;
import com.iot.common.exception.ServiceException;
import com.iot.common.exception.UnAuthorizedException;
import com.iot.common.web.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 全局异常处理
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler( HttpRequestMethodNotSupportedException.class )
    @ResponseStatus( HttpStatus.METHOD_NOT_ALLOWED )
    protected R< ? > handleException( HttpRequestMethodNotSupportedException ex ) {
        return R.custom( HttpStatus.METHOD_NOT_ALLOWED.value(), "错误的请求方式" );
    }

    @ExceptionHandler( HttpMediaTypeNotSupportedException.class )
    @ResponseStatus( HttpStatus.UNSUPPORTED_MEDIA_TYPE )
    protected R< ? > handleException( HttpMediaTypeNotSupportedException ex ) {
        return R.custom( HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), "不支持的资源类型" );
    }

    @ExceptionHandler( HttpMediaTypeNotAcceptableException.class )
    @ResponseStatus( HttpStatus.NOT_ACCEPTABLE )
    protected R< ? > handleException( HttpMediaTypeNotAcceptableException ex ) {
        return R.custom( HttpStatus.NOT_ACCEPTABLE.value(), "不接受的响应资源类型" );
    }

    @ExceptionHandler( MissingPathVariableException.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    protected R< ? > handleException( MissingPathVariableException ex ) {
        return R.custom( HttpStatus.INTERNAL_SERVER_ERROR.value(), "路径变量名不匹配" );
    }

    @ExceptionHandler( MissingServletRequestParameterException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( MissingServletRequestParameterException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "缺少参数:" + ex.getParameterName() + "---参数类型:" + ex.getParameterType() );
    }

    @ExceptionHandler( ServletRequestBindingException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( ServletRequestBindingException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "未绑定参数" );
    }

    @ExceptionHandler( ConversionNotSupportedException.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    protected R< ? > handleException( ConversionNotSupportedException ex ) {
        return R.custom( HttpStatus.INTERNAL_SERVER_ERROR.value(), "对象类型转换错误" );
    }

    @ExceptionHandler( TypeMismatchException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( TypeMismatchException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "对象注入失败" );
    }

    @ExceptionHandler( { InvalidFormatException.class, HttpMessageNotReadableException.class } )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( HttpMessageNotReadableException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "参数类型错误" );
    }

    @ExceptionHandler( HttpMessageNotWritableException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( HttpMessageNotWritableException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "返回信息类型错误" );
    }

    @ExceptionHandler( { MethodArgumentNotValidException.class } )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( MethodArgumentNotValidException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "参数:" + ex.getParameter().getParameterName() + "校验错误" );
    }

    @ExceptionHandler( MissingServletRequestPartException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( MissingServletRequestPartException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "multipart/form-data请求部分缺失" );
    }

    @ExceptionHandler( BindException.class )
    @ResponseStatus( HttpStatus.BAD_REQUEST )
    protected R< ? > handleException( BindException ex ) {
        return R.custom( HttpStatus.BAD_REQUEST.value(), "参数校验错误" );
    }

    @ExceptionHandler( { NotFoundException.class, NoHandlerFoundException.class } )
    @ResponseStatus( HttpStatus.NOT_FOUND )
    protected R< ? > handleException( NoHandlerFoundException ex ) {
        return R.custom( HttpStatus.NOT_FOUND.value(), ex.getHttpMethod() + " " + ex.getRequestURL() + "找不到处理程序" );
    }

    @ExceptionHandler( AsyncRequestTimeoutException.class )
    @ResponseStatus( HttpStatus.SERVICE_UNAVAILABLE )
    protected R< ? > handleException( AsyncRequestTimeoutException ex ) {
        return R.custom( HttpStatus.SERVICE_UNAVAILABLE.value(), "请求超时" );
    }

    // @ExceptionHandler( ImportException.class )
    // @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    // protected R< ? > handleException( ImportException ex ) {
    //     return R.custom( 500, ex.getMessage() );
    // }

    // @ExceptionHandler( TransException.class )
    // @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    // protected R< ? > handleException( TransException e ) {
    //     return R.custom( 500, e.getMessage() );
    // }

    @ExceptionHandler( UnAuthorizedException.class )
    @ResponseStatus( HttpStatus.UNAUTHORIZED )
    public R< String > unAuthorizedException( UnAuthorizedException unAuthorizedException ) {
        log.warn( "UnAuthorized Exception Handler: {}", unAuthorizedException.getMessage(), unAuthorizedException );
        return R.failed( unAuthorizedException.getMessage() );
    }

    @ExceptionHandler( JsonException.class )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    protected R< ? > handleException( JsonException ex ) {
        return R.custom( HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage() );
    }

    @ExceptionHandler( { ServiceException.class, Exception.class } )
    @ResponseStatus( HttpStatus.INTERNAL_SERVER_ERROR )
    protected R< ? > handleException( Exception ex ) {
        return R.custom( HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage() );
    }

}
