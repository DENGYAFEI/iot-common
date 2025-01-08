package com.iot.common.excel.model.annotation;

import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Orchid
 * @Create 2023/11/25
 * @Remark
 */
@Target( ElementType.FIELD)
@Retention( RetentionPolicy.RUNTIME )
public @interface ExcelRowStyle{

    /**
     * 文本换行 默认跟随ExcelStyle的wrap
     * cellStyle.setWrapText( wrap == 0 ? excelStyle.wrap() : wrap > 0 );
     */
    int wrap() default 0;

    /**
     * 文本加粗 默认跟随ExcelStyle的bold
     * font.setBold( bold == 0 ? excelStyle.bold() : bold > 0 );
     */
    int bold() default 0;

    /**
     * 横向对齐方式
     */
    HorizontalAlignment horizontalAlign() default HorizontalAlignment.CENTER;

    /**
     * 纵向对齐方式
     */
    VerticalAlignment verticalAlign() default VerticalAlignment.CENTER;

    /**
     * 文字颜色 默认为 IndexedColors.BLACK
     */
    IndexedColors color() default IndexedColors.BLACK;

    /**
     * 背景颜色 默认为 IndexedColors.WHITE
     */
    IndexedColors backgroundColor() default IndexedColors.WHITE;

}
