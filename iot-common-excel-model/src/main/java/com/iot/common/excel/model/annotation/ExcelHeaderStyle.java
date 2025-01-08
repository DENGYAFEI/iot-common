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
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface ExcelHeaderStyle {

    /**
     * 列宽
     */
    int width() default 0;

    ExcelRowStyle rowStyle()  default @ExcelRowStyle();
}
