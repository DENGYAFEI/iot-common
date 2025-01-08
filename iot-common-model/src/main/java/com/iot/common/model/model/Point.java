package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.constant.DictConstant;
import com.iot.common.enums.EnableFlagEnum;
import com.iot.common.enums.PointDataTypeEnum;
import com.iot.common.mysql.model.pojo.entity.PermissionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 设备点位表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_point" )
@Schema( description = "设备点位表实体" )
public class Point extends PermissionEntity {

    /**
     * 点位类型
     */
    @Schema( description = "点位类型" )
    @TableField( "point_type" )
    private String pointType;

    /**
     * 点位类别
     */
    @Schema( description = "点位类别" )
    @TableField( "point_category" )
    private String pointCategory;

    /**
     * 点位名称
     */
    @Schema( description = "点位名称", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "point_name" )
    private String pointName;

    /**
     * 点位编号
     */
    @Schema( description = "点位编号" )
    @TableField( "point_code" )
    private String pointCode;

    /**
     * 点位数据类型
     */
    @Schema( description = "点位数据类型" )
    @TableField( "point_data_type" )
    private String pointDataType;

    /**
     * 基础值
     */
    @Schema( description = "基础值" )
    @TableField( "base_value" )
    private BigDecimal baseValue;

    /**
     * 比例系数
     */
    @Schema( description = "比例系数" )
    @TableField( "multiple" )
    private BigDecimal multiple;

    /**
     * 数据精度
     */
    @Schema( description = "数据精度" )
    @TableField( "value_decimal" )
    private Integer valueDecimal;

    /**
     * 单位
     */
    @Schema( description = "单位" )
    @TableField( "unit" )
    private String unit;

    /**
     * 使能标识
     */
    @Schema( description = "使能标识" )
    @TableField( "enable_flag" )
    private EnableFlagEnum enableFlag;

    /**
     * 模板ID
     */
    @Schema( description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED )
    // @TableField( "profile_id" )
    @TableField( exist = false )
    private String profileId;

    /**
     * 设置默认值
     */
    public void setDefault() {
        this.pointDataType = PointDataTypeEnum.STRING.getType();
        this.multiple = BigDecimal.valueOf( 1 );
        this.valueDecimal = 6;
        this.unit = "";
    }

}
