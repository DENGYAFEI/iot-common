package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import com.iot.common.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 系统字典
 *
 * @TableName sys_dict
 */
@EqualsAndHashCode( callSuper = true )
@TableName( value = "sys_dict" )
@Data
public class SysDict extends BaseEntity implements Serializable {

    /**
     * 字典编码
     */
    @TableField( value = "code" )
    private String code;

    /**
     * 字典名称
     */
    @TableField( value = "name" )
    private String name;

    /**
     * 字典备注
     */
    @TableField( value = "remark" )
    private String remark;

    /**
     * 启用标识
     */
    @TableField( value = "enable_flag" )
    private EnableFlagEnum enableFlag;

    @TableField( exist = false )
    private static final long serialVersionUID = 1L;
}
