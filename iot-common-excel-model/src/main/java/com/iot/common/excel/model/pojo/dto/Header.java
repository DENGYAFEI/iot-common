package com.iot.common.excel.model.pojo.dto;

import lombok.Data;

/**
 * @Author Orchid
 * @Create 2024/6/1
 * @Remark
 */
@Data
public class Header {

    /**
     * 字段名
     */
    private String column;

    /**
     * 是否展示
     */
    private Boolean isShow;

    /**
     * 排序字段 值越小越靠前
     */
    private Integer order;

}
