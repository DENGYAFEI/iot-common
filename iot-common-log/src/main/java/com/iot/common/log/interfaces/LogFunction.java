package com.iot.common.log.interfaces;

/**
 * @Author Orchid
 * @Create 2024/4/1
 * @Remark 以此接口来代替log.info等方法
 */
@FunctionalInterface
public interface LogFunction {

    void log( String msg, Object... args );

}
