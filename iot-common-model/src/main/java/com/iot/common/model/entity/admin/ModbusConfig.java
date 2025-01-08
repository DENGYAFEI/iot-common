package com.iot.common.model.entity.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * modbus点位配置表
 * @TableName modbus_config
 */
@TableName(value ="modbus_config")
@Data
public class ModbusConfig implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 设备点位id
     */
    private String device_point_id;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口号
     */
    private String port;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 最近操作时间
     */
    private Date last_update_time;

    /**
     * 最近操作者
     */
    private String last_operator;

    /**
     * 版本号
     */
    private Long verison;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}