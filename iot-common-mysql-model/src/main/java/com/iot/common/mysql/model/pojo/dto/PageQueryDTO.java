package com.iot.common.mysql.model.pojo.dto;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @创建人 zsy
 * @创建时间 2022-11-07
 * @描述 没有描述
 */

@Schema( description = "PageQueryDTO" )
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageQueryDTO {

    @Schema( description = "当前页码" )
    private Integer page = 1;

    @Schema( description = "每页数量" )
    private Integer limit = 20;

    @Schema( description = "排序字段" )
    private List< OrderItem > orders;

    @Schema( description = "筛选条件" )
    private List< FilterItem > filters;

    @Schema( description = "其他条件,默认=" )
    private Map< String, Object > condition;

}
