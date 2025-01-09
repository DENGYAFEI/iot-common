package com.iot.common.model.entity.task;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
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
    @TableField(value = "bean_name")
    private String beanName;

    /**
     * 方法名称
     */
    @TableField(value = "method_name")
    private String methodName;

    /**
     * 方法参数
     */
    @TableField(value = "method_params")
    private String methodParams;

    /**
     * cron表达式
     */
    @TableField(value = "cron_expression")
    private String cronExpression;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 创建者
     */
    @TableField(value = "creator")
    private String creator;

    /**
     * 最近操作时间
     */
    @TableField(value = "last_update_time")
    private LocalDateTime lastUpdateTime;

    /**
     * 最近操作者
     */
    @TableField(value = "last_operator")
    private String lastOperator;

    /**
     * 版本号
     */
    @TableField(value = "version")
    private Long version;

    /**
     * 状态
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}