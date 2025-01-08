package com.iot.common.mysql.model.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @创建人 zsy
 * @创建时间 2023-02-24
 * @描述 没有描述
 */
@Schema( description = "FilterItem" )
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterItem {

    @Schema( description = "字段名")
    private String column;
    @Schema( description = "过滤类型")
    private String type;
    @Schema( description = "值")
    private String value;
    @Schema( description = "操作符")
    private String operator;

}
