package com.iot.common.model.entity.admin;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户表
 * @TableName user
 */
@TableName(value ="user")
@Data
public class User implements Serializable {
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
     * 姓名
     */
    private String name;

    /**
     * 密码
     */
    private String password;

    /**
     * 创建时间
     */
    private Date create_time;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 最近更新时间
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