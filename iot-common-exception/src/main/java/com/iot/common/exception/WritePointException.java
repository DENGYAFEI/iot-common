package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 自定义 写入位号值 异常
 */
public class WritePointException extends RuntimeException {

    public WritePointException() {
        this( null );
    }

    public WritePointException( Throwable cause ) {
        super( cause );
    }

    public WritePointException( CharSequence template, Object... params ) {
        super( CharSequenceUtil.format( template, params ) );
    }

}
