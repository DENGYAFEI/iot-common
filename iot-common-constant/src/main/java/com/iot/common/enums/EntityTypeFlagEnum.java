package com.iot.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 通用实体类型标识枚举
 */
@Getter
@AllArgsConstructor
public enum EntityTypeFlagEnum {

    /**
     * 系统
     */
    SYSTEM( "system", "系统" ),

    /**
     * 用户
     */
    USER( "user", "用户" ),

    /**
     * 分组
     */
    GROUP( "group", "分组" ),

    /**
     * 驱动
     */
    DRIVER( "driver", "驱动" ),

    /**
     * 模板
     */
    PROFILE( "profile", "模板" ),

    /**
     * 位号
     */
    POINT( "point", "位号" ),

    /**
     * 设备
     */
    DEVICE( "device", "设备" ),
    ;

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
     * @return EntityTypeFlagEnum
     */
    public static EntityTypeFlagEnum ofCode( String code ) {
        Optional< EntityTypeFlagEnum > any = Arrays.stream( EntityTypeFlagEnum.values() ).filter( type -> type.getCode().equals( code ) ).findFirst();
        return any.orElse( null );
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return EntityTypeFlagEnum
     */
    public static EntityTypeFlagEnum ofName( String name ) {
        try {
            return valueOf( name );
        } catch ( IllegalArgumentException e ) {
            return null;
        }
    }

}
