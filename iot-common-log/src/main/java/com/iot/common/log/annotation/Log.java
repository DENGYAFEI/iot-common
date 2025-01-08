package com.iot.common.log.annotation;

import com.iot.common.log.enums.LogType;
import com.iot.common.log.factory.DefaultLogFactory;
import com.iot.common.log.factory.LogFactory;

import java.lang.annotation.*;

/**
 * @Author Orchid
 * @Create 2024/4/1
 * @Remark
 */
@Documented
@Inherited
@Target( ElementType.METHOD )
@Retention( RetentionPolicy.RUNTIME )
public @interface Log {

    Class< ? extends LogFactory > logFactory() default DefaultLogFactory.class;

    LogType level() default LogType.INFO;

    String tag() default "";

    boolean save() default false;

}
