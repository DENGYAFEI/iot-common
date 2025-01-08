package com.iot.common.model.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 采集任务表
 * @TableName gather_job
 */
@TableName(value ="gather_job")
@Data
public class GatherJob implements Serializable {
    /**
     * id
     */
    @TableId
    private String id;

    /**
     * bean名称
     */
    private String bean_name;

    /**
     * 方法名称
     */
    private String method_name;

    /**
     * 方法参数
     */
    private String method_params;

    /**
     * cron表达式
     */
    private String cron_expression;

    /**
     * 备注
     */
    private String remark;

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

    /**
     * 状态
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}