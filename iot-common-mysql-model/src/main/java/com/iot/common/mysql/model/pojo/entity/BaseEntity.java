package com.iot.common.mysql.model.pojo.entity;

import cn.hutool.core.date.DatePattern;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @Author Orchid
 * @Create 2024/4/9
 * @Remark entity实体公共类
 */
@Data
@EqualsAndHashCode( callSuper = true )
public class BaseEntity extends Base {

    /**
     * 创建人
     */
    @Schema( description = "创建人" )
    @TableField( value = "creator", fill = FieldFill.INSERT )
    private String creator;

    /**
     * 创建时间
     */
    @Schema( description = "创建时间" )
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = DatePattern.NORM_DATETIME_PATTERN )
    @DateTimeFormat( pattern = DatePattern.NORM_DATETIME_PATTERN )
    @TableField( value = "create_time", fill = FieldFill.INSERT )
    private LocalDateTime createTime;

    /**
     * 最后修改人
     */
    @Schema( description = "最后修改人" )
    @TableField( value = "last_operator", fill = FieldFill.INSERT_UPDATE )
    private String lastOperator;

    /**
     * 最后修改时间
     */
    @Schema( description = "最后修改时间" )
    @JsonFormat( shape = JsonFormat.Shape.STRING, pattern = DatePattern.NORM_DATETIME_PATTERN )
    @DateTimeFormat( pattern = DatePattern.NORM_DATETIME_PATTERN )
    @TableField( value = "last_update_time", fill = FieldFill.INSERT_UPDATE )
    private LocalDateTime lastUpdateTime;

    /**
     * 逻辑删标识
     * <ul>
     * <li>0 (false):默认,未删除</li>
     * <li>1 (true):已删除</li>
     * </ul>
     */
    // @TableLogic
    // @TableField( select = false )
    // private Integer deleted;

}

