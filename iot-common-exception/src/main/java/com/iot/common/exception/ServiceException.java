package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 服务异常
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
        this( null );
    }

    public ServiceException( Throwable cause ) {
        super( cause );
    }

    public ServiceException( CharSequence template, Object... params ) {
        super( CharSequenceUtil.format( template, params ) );
    }

}
