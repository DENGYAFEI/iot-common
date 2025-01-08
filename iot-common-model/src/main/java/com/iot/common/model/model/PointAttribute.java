package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import com.iot.common.enums.AttributeFormTypeEnum;
import com.iot.common.enums.AttributeTypeFlagEnum;
import com.iot.common.model.valid.Insert;
import com.iot.common.model.valid.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 点位配置信息表实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_point_attribute" )
@Schema( description = "点位配置信息表实体" )
public class PointAttribute extends BaseEntity {

    /**
     * 显示名称
     */
    @Schema( description = "显示名称", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Display name can't be empty",
            groups = { Insert.class } )
    @Pattern(
            regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid display name",
            groups = { Insert.class, Update.class }
    )
    @TableField( "display_name" )
    private String displayName;

    /**
     * 属性名称
     */
    @Schema( description = "属性名称", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Attribute name can't be empty",
            groups = { Insert.class }
    )
    @Pattern(
            regexp = "^[A-Za-z0-9][A-Za-z0-9-_#@/.|]{1,31}$",
            message = "Invalid attribute name",
            groups = { Insert.class, Update.class }
    )
    @TableField( "attribute_name" )
    private String attributeName;

    /**
     * 属性类型标识
     */
    @Schema( description = "属性类型标识" )
    @TableField( "attribute_type_flag" )
    private AttributeTypeFlagEnum attributeTypeFlag;

    /**
     * 属性表单类型
     */
    @Schema( description = "属性表单类型" )
    @TableField( "attribute_form_type" )
    private AttributeFormTypeEnum attributeFormType;

    /**
     * 属性表单数据
     */
    @Schema( description = "属性表单数据" )
    @TableField( "attribute_form_data" )
    private String attributeFormData;

    /**
     * 排序值
     */
    @Schema( description = "排序值" )
    @TableField( "order_num" )
    private Integer orderNum;

    /**
     * 默认值
     */
    @Schema( description = "默认值" )
    @TableField( "default_value" )
    private String defaultValue;

    /**
     * 驱动ID
     */
    @Schema( description = "驱动ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotNull(
            message = "Driver id can't be empty",
            groups = { Insert.class, Update.class }
    )
    @TableField( "driver_id" )
    private String driverId;

}
