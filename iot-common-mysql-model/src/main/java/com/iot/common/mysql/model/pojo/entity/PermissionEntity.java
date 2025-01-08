package com.iot.common.mysql.model.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author Orchid
 * @Create 2024/7/25
 * @Remark
 */
@EqualsAndHashCode( callSuper = true )
@Data
public class PermissionEntity extends BaseEntity {

    /**
     * 组织架构ID
     */
    @Schema( description = "组织架构ID" )
    @TableField( value = "org_id", fill = FieldFill.INSERT )
    private String orgId;

}
