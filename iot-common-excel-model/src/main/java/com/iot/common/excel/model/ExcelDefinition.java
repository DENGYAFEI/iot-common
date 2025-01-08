package com.iot.common.excel.model;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author Orchid
 * @Create 2023/11/25
 * @Remark
 */
@Data
public class ExcelDefinition {

    private String fileName;

    private ExcelStyleDefinition styleDefinition;

    private Map< String, List< List< Object > > > sheetMap;

    private Map< Integer, List< String > > selectMap;

}
