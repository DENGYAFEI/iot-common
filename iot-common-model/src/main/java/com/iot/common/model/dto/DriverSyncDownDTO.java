package com.iot.common.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 元数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverSyncDownDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 待同步数据内容
     */
    private String content;

}
