package com.iot.common.excel.model;

import com.iot.common.excel.model.annotation.ExcelHeaderStyle;
import com.iot.common.excel.model.annotation.ExcelRowStyle;
import com.iot.common.excel.model.annotation.ExcelStyle;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author Orchid
 * @Create 2023/11/25
 * @Remark
 */
@Data
public class ExcelStyleDefinition {

    private ExcelStyle excelStyle;

    private Map< Integer, ExcelHeaderStyle > headerStyleMap = new HashMap<>();

    private Map< Integer, ExcelRowStyle > rowStyleMap = new HashMap<>();

}
