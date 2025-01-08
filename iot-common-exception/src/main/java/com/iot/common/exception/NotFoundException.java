package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 资源未找到异常
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        this( null );
    }

    public NotFoundException( Throwable cause ) {
        super( cause );
    }

    public NotFoundException( CharSequence template, Object... params ) {
        super( CharSequenceUtil.format( template, params ) );
    }

}
