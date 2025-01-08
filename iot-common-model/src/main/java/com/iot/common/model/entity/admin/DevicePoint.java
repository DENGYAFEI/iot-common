package com.iot.common.model.entity.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备点位表
 * @TableName device_point
 */
@TableName(value ="device_point")
@Data
public class DevicePoint implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 设备id
     */
    private String device_id;

    /**
     * 定时任务id
     */
    private String gather_job_id;

    /**
     * 点位名称
     */
    private String point_name;

    /**
     * 驱动编码
     */
    private String driver_code;

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
    private Long version;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}