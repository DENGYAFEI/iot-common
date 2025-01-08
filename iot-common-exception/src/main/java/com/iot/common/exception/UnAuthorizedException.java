package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 未登录异常
 */
public class UnAuthorizedException extends RuntimeException {

    public UnAuthorizedException() {
        this( null );
    }

    public UnAuthorizedException( Throwable cause ) {
        super( cause );
    }

    public UnAuthorizedException( CharSequence template, Object... params ) {
        super( CharSequenceUtil.format( template, params ) );
    }

}
