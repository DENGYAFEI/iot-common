package com.iot.common.excel.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Orchid
 * @Create 2023/11/25
 * @Remark
 */
@Target( ElementType.TYPE )
@Retention( RetentionPolicy.RUNTIME )
public @interface ExcelStyle {

    /**
     * 导出列宽 默认15
     */
    int width() default 15;

    /**
     * 文本换行 默认false
     */
    boolean wrap() default false;

    /**
     * 文本加粗 默认false
     */
    boolean bold() default false;

    /**
     * 是否启用边框
     */
    boolean border() default false;

}
