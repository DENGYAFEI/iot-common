package com.iot.common.mysql.model.pojo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.iot.common.mysql.model.anotation.TableVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 基础 Entity 实体类
 */
@TableVO
@Data
public class Base implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId( value = "id", type = IdType.ASSIGN_ID )
    @Schema( name = "id", title = "主键" )
    private String id;

}
