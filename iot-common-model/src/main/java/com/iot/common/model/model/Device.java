package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.enums.EnableFlagEnum;
import com.iot.common.excel.model.annotation.ExcelExport;
import com.iot.common.model.valid.Insert;
import com.iot.common.model.valid.Update;
import com.iot.common.mysql.model.pojo.entity.PermissionEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 设备表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_device" )
@Schema( description = "设备表实体" )
public class Device extends PermissionEntity {

    /**
     * BASIC设备ID
     */
    @Schema( description = "BASIC设备ID" )
    @NotBlank(
        message = "所属设备不能为空",
        groups = { Insert.class, Update.class }
    )
    @ExcelExport( value = "设备", trans = true )
    @TableField( "basic_device" )
    private String basicDevice;

    /**
     * BASIC设备编码
     */
    @Schema( description = "BASIC设备编码" )
    @NotBlank(
        message = "所属设备编码不能为空",
        groups = { Insert.class, Update.class }
    )
    @ExcelExport( value = "设备编码", trans = true )
    @TableField( "basic_device_code" )
    private String basicDeviceCode;

    /**
     * 设备编号
     */
    @Schema( description = "设备编号" )
    @ExcelExport( value = "设备唯一编号" )
    @TableField( "device_code" )
    private String deviceCode;

    /**
     * 设备名称
     */
    //@Schema( description = "设备名称", requiredMode = Schema.RequiredMode.REQUIRED )
    //@NotBlank(
    //        message = "设备名称不能为空",
    //        groups = { Insert.class, Update.class }
    //)
    //@TableField( "device_name" )
    //private String deviceName;

    /**
     * 驱动ID
     */
    @Schema( description = "驱动ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
        message = "驱动不能为空",
        groups = { Insert.class, Update.class }
    )
    @TableField( "driver_id" )
    private String driverId;

    /**
     * 采集标识
     */
    @Schema( description = "采集标识" )
    @ExcelExport( value = "是否采集", kv = "ENABLE-是;DISABLE-否" )
    @TableField( "gather_flag" )
    private EnableFlagEnum gatherFlag;

    /**
     * 使能标识
     */
    @Schema( description = "使能标识" )
    @ExcelExport( value = "是否启用", kv = "ENABLE-是;DISABLE-否" )
    @TableField( "enable_flag" )
    private EnableFlagEnum enableFlag;

    /**
     * 设备描述
     */
    @Schema( description = "设备描述" )
    @ExcelExport( value = "设备描述" )
    @TableField( "remark" )
    private String remark;

    /**
     * 关联模板
     */
    @Schema( description = "关联模板" )
    @TableField( exist = false )
    private Set< String > profileIds = new HashSet<>( 8 );

}
