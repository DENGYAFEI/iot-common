package com.iot.common.model.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iot.common.mysql.model.pojo.entity.BaseEntity;
import com.iot.common.enums.EnableFlagEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @TableName sys_dict_item
 */
@EqualsAndHashCode( callSuper = true )
@TableName( value = "sys_dict_item" )
@Data
public class SysDictItem extends BaseEntity implements Serializable {

    /**
     * 字典ID
     */
    @TableField( value = "dict_id" )
    private String dictId;

    /**
     * 字典项编码
     */
    @TableField( value = "code" )
    private String code;

    /**
     * 字典项名称
     */
    @TableField( value = "name" )
    private String name;

    /**
     * 提交值
     */
    @TableField( value = "value" )
    private String value;

    /**
     * 排序
     */
    @TableField( value = "order_num" )
    private Integer orderNum;

    /**
     * 启用标识
     */
    @TableField( value = "enable_flag" )
    private EnableFlagEnum enableFlag;

    @TableField( exist = false )
    private static final long serialVersionUID = 1L;
}
