package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 驱动属性配置表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_driver_attribute_config" )
@Schema( description = "驱动属性配置表实体" )
public class DriverAttributeConfig extends BaseEntity {

    /**
     * 连接配置ID
     */
    @Schema( description = "连接配置ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "driver_attribute_id" )
    private String driverAttributeId;

    /**
     * 配置值
     */
    @Schema( description = "配置值", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "config_value" )
    private String configValue;

    /**
     * 设备ID
     */
    @Schema( description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "device_id" )
    private String deviceId;

    public DriverAttributeConfig( String id, String driverAttributeId, String configValue, String deviceId ) {
        setId( id );
        this.driverAttributeId = driverAttributeId;
        this.configValue = configValue;
        this.deviceId = deviceId;
    }
}
