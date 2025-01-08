package com.iot.common.model.entity.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备表
 * @TableName device
 */
@TableName(value ="device")
@Data
public class Device implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * 组织id
     */
    private String org_id;

    /**
     * 设备编码
     */
    private String device_code;

    /**
     * 设备名称
     */
    private String device_name;

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