package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 点位属性配置表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_point_attribute_config" )
@Schema( description = "点位属性配置表实体" )
public class PointAttributeConfig extends BaseEntity {

    /**
     * 位号配置ID
     */
    @Schema( description = "位号配置ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "point_attribute_id" )
    private String pointAttributeId;

    /**
     * 位号配置值
     */
    @Schema( description = "位号配置值", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "config_value" )
    private String configValue;

    /**
     * 设备ID
     */
    @Schema( description = "设备ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "device_id" )
    private String deviceId;

    /**
     * 点位ID
     */
    @Schema( description = "点位ID", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "point_id" )
    private String pointId;

    public PointAttributeConfig( String id, String pointAttributeId, String configValue, String deviceId, String pointId ) {
        setId( id );
        this.pointAttributeId = pointAttributeId;
        this.configValue = configValue;
        this.deviceId = deviceId;
        this.pointId = pointId;
    }

}
