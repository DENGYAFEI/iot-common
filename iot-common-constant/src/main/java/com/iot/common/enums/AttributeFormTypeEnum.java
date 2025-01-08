package com.iot.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author Orchid
 * @Create 2024/4/30
 * @Remark
 */
@Getter
public enum AttributeFormTypeEnum {

    /**
     * 字符串
     */
    STRING( "string", "字符串" ),
    /**
     * 字节
     */
    BYTE( "byte", "字节" ),
    /**
     * 短整数
     */
    SHORT( "short", "短整数" ),
    /**
     * 整数
     */
    INT( "int", "整数" ),
    /**
     * 长整数
     */
    LONG( "long", "长整数" ),
    /**
     * 浮点数
     */
    FLOAT( "float", "浮点数" ),
    /**
     * 双精度浮点数
     */
    DOUBLE( "double", "双精度浮点数" ),
    /**
     * 布尔量
     */
    BOOLEAN( "boolean", "布尔量" ),
    /**
     * 下拉框
     */
    SELECT( "select", "下拉框" ),
    ;

    AttributeFormTypeEnum( String code, String name ) {
        this.code = code;
        this.name = name;
    }

    @EnumValue
    private final String code;
    private final String name;

    /**
     * 根据 Code 获取枚举
     *
     * @param code Code
     * @return AttributeFormTypeEnum
     */
    public static AttributeFormTypeEnum ofCode( String code ) {
        Optional< AttributeFormTypeEnum > any = Arrays.stream( AttributeFormTypeEnum.values() ).filter( type -> type.getCode().equals( code ) ).findFirst();
        return any.orElse( null );
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return AttributeFormTypeEnum
     */
    public static AttributeFormTypeEnum ofName( String name ) {
        try {
            return valueOf( name );
        } catch ( IllegalArgumentException e ) {
            return null;
        }
    }

}
