package com.iot.common.model.dto;


import com.iot.common.enums.MetadataCommandTypeEnum;
import com.iot.common.enums.MetadataTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 元数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverMetadataDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 元数据类型
     */
    private MetadataTypeEnum type;

    /**
     * 元数据操作类型
     */
    private MetadataCommandTypeEnum metadataCommandType;

    /**
     * 元数据内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createTime;

    public DriverMetadataDTO(MetadataTypeEnum type, MetadataCommandTypeEnum metadataCommandType, String content) {
        this.type = type;
        this.metadataCommandType = metadataCommandType;
        this.content = content;
        this.createTime = new Date();
    }
}
