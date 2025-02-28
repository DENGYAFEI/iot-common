package com.iot.common.excel.model.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Orchid
 * @Create 2023/10/20
 * @Remark
 */
@Target( ElementType.FIELD )
@Retention( RetentionPolicy.RUNTIME )
public @interface ExcelExport {

    /**
     * 字段名称
     */
    String value();

    /**
     * 导出排序先后: 数字越小越靠前（默认按Java类字段顺序导出）
     */
    int sort() default 0;

    /**
     * 导出映射，格式如：0-未知;1-男;2-女
     */
    String kv() default "";

    /**
     * 导出模板示例值（有值的话，直接取该值，不做映射）
     */
    String example() default "";

    /**
     * 是否为翻译字段 为翻译处理预留 此字段为true的将不会被导出
     */
    boolean trans() default false;

}
