package com.iot.common.exception;

import cn.hutool.core.text.CharSequenceUtil;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 自定义 读取位号值 异常
 */
public class ReadPointException extends RuntimeException {

    public ReadPointException() {
        this( null );
    }

    public ReadPointException( Throwable cause ) {
        super( cause );
    }

    public ReadPointException( CharSequence template, Object... params ) {
        super( CharSequenceUtil.format( template, params ) );
    }

}
