package com.iot.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 通用属性类型标识枚举
 */
@Getter
@AllArgsConstructor
public enum AttributeTypeFlagEnum {

    /**
     * 字符串
     */
    STRING( "0", "string", "字符串" ),

    /**
     * 字节
     */
    BYTE( "1", "byte", "字节" ),

    /**
     * 短整数
     */
    SHORT( "2", "short", "短整数" ),

    /**
     * 整数
     */
    INT( "3", "int", "整数" ),

    /**
     * 长整数
     */
    LONG( "4", "long", "长整数" ),

    /**
     * 浮点数
     */
    FLOAT( "5", "float", "浮点数" ),

    /**
     * 双精度浮点数
     */
    DOUBLE( "6", "double", "双精度浮点数" ),

    /**
     * 布尔量
     */
    BOOLEAN( "7", "boolean", "布尔量" ),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final String type;

    /**
     * 编码
     */
    private final String code;

    /**
     * 备注
     */
    private final String remark;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return AttributeTypeFlagEnum
     */
    public static AttributeTypeFlagEnum ofCode( String code ) {
        Optional< AttributeTypeFlagEnum > any = Arrays.stream( AttributeTypeFlagEnum.values() ).filter( type -> type.getCode().equals( code ) ).findFirst();
        return any.orElse( null );
    }

    /**
     * 根据 Index 获取枚举
     *
     * @param typeStr Code
     * @return AttributeTypeFlagEnum
     */
    public static AttributeTypeFlagEnum ofType( String typeStr ) {
        Optional< AttributeTypeFlagEnum > any = Arrays.stream( AttributeTypeFlagEnum.values() ).filter( type -> type.getType().equals( typeStr ) ).findFirst();
        return any.orElse( null );
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return AttributeTypeFlagEnum
     */
    public static AttributeTypeFlagEnum ofName( String name ) {
        try {
            return valueOf( name );
        } catch ( IllegalArgumentException e ) {
            return null;
        }
    }

}
