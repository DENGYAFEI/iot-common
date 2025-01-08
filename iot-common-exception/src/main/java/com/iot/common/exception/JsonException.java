package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark Json异常
 */
public class JsonException extends RuntimeException {

    public JsonException() {
        this( null );
    }

    public JsonException( Throwable cause ) {
        super( cause );
    }

    public JsonException( CharSequence template, Object... params ) {
        super( CharSequenceUtil.format( template, params ) );
    }

}
