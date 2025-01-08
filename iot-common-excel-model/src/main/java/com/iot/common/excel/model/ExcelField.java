package com.iot.common.excel.model;

import com.iot.common.excel.model.annotation.ExcelHeaderStyle;
import com.iot.common.excel.model.annotation.ExcelRowStyle;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * @Author Orchid
 * @Create 2023/10/20
 * @Remark
 */
@Data
public class ExcelField {

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 字段类型
     */
    private Class< ? > fieldType;

    /**
     * 表头名称
     */
    private String name;

    /**
     * 映射关系
     */
    private LinkedHashMap< String, String > kvMap;

    /**
     * 是否翻译
     */
    private boolean isTrans;

    /**
     * 示例值
     */
    private Object example;

    /**
     * 排序
     */
    private int sort;

    /**
     * 是否为注解字段：0-否，1-是
     */
    private boolean hasAnnotation;

    private ExcelHeaderStyle excelHeaderStyle;

    private ExcelRowStyle excelRowStyle;

}
