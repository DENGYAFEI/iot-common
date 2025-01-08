package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.enums.EnableFlagEnum;
import com.iot.common.mysql.model.pojo.entity.PermissionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 模板表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_profile" )
@Schema( description = "模板表实体" )
public class Profile extends PermissionEntity {

    /**
     * 模板编号
     */
    @Schema( description = "模板编号" )
    @TableField( "profile_code" )
    private String profileCode;

    /**
     * 模板名称
     */
    @Schema( description = "模板名称", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "profile_name" )
    private String profileName;

    /**
     * 使能标识
     */
    @Schema( description = "使能标识" )
    @TableField( "enable_flag" )
    private EnableFlagEnum enableFlag;

    /**
     * 模板描述
     */
    @Schema( description = "模板描述" )
    @TableField( "remark" )
    private String remark;

}
