package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import com.iot.common.model.valid.Insert;
import com.iot.common.model.valid.Update;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 驱动表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
@TableName( "iot_driver" )
@Schema( description = "驱动表实体" )
public class Driver extends BaseEntity {

    /**
     * 驱动编号
     */
    @Schema( description = "驱动编号" )
    @TableField( "driver_code" )
    private String driverCode;

    /**
     * 驱动名称
     */
    @Schema( description = "驱动名称", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Driver name can't be empty",
            groups = { Insert.class }
    )
    @Pattern(
            regexp = "^[A-Za-z0-9\\u4e00-\\u9fa5][A-Za-z0-9\\u4e00-\\u9fa5-_#@/.|]{1,31}$",
            message = "Invalid driver name",
            groups = { Insert.class, Update.class }
    )
    @TableField( "driver_name" )
    private String driverName;

    /**
     * 驱动服务名称
     */
    @Schema( description = "驱动服务名称", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Service name can't be empty",
            groups = { Insert.class }
    )
    @Pattern(
            regexp = "^[A-Za-z0-9][A-Za-z0-9\\-_#@/.|]{1,31}$",
            message = "Invalid service name",
            groups = { Insert.class, Update.class }
    )
    @TableField( "service_name" )
    private String serviceName;

    /**
     * 服务主机
     */
    @Schema( description = "服务主机", requiredMode = Schema.RequiredMode.REQUIRED )
    @NotBlank(
            message = "Service host can't be empty",
            groups = { Insert.class }
    )
    @Pattern(
            regexp = "^((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}$",
            message = "Invalid service host",
            groups = { Insert.class, Update.class }
    )
    @TableField( "service_host" )
    private String serviceHost;

    /**
     * 驱动自定义转换配置
     */
    @Schema( description = "驱动自定义转换配置" )
    @TableField( "driver_convert" )
    private String driverConvert;

    /**
     * 驱动备注
     */
    @Schema( description = "驱动备注", requiredMode = Schema.RequiredMode.REQUIRED )
    @TableField( "remark" )
    private String remark;

}
