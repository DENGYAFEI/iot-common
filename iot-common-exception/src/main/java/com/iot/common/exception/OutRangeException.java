package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 自定义 超出范围 异常
 */
public class OutRangeException extends RuntimeException {

    public OutRangeException() {
        this( null );
    }

    public OutRangeException( Throwable cause ) {
        super( cause );
    }

    public OutRangeException( CharSequence template, Object... params ) {
        super( getOutRangeMessage( template, params ) );
    }

    private static String getOutRangeMessage( CharSequence template, Object... params ) {
        return CharSequenceUtil.format( template, params );
    }

}
