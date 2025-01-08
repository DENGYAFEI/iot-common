package com.iot.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 通用元数据类型枚举
 */
@Getter
@AllArgsConstructor
public enum MetadataTypeEnum {

    /**
     * 元数据
     */
    METADATA( ( byte ) 0x00, "metadata", "元数据" ),

    /**
     * 模板类型元数据
     */
    PROFILE( ( byte ) 0x01, "profile", "模板类型元数据" ),

    /**
     * 位号类型元数据
     */
    POINT( ( byte ) 0x02, "point", "位号类型元数据" ),

    /**
     * 设备类型元数据
     */
    DEVICE( ( byte ) 0x03, "device", "设备类型元数据" ),

    /**
     * 驱动属性配置类型元数据
     */
    DRIVER_ATTRIBUTE_CONFIG( ( byte ) 0x04, "driver_attribute_config", "驱动属性配置类型元数据" ),

    /**
     * 位号属性配置类型元数据
     */
    POINT_ATTRIBUTE_CONFIG( ( byte ) 0x05, "point_attribute_config", "位号属性配置类型元数据" ),
    ;

    /**
     * 索引
     */
    @EnumValue
    private final Byte index;

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
     * @return MetadataTypeEnum
     */
    public static MetadataTypeEnum ofCode( String code ) {
        Optional< MetadataTypeEnum > any = Arrays.stream( MetadataTypeEnum.values() ).filter( type -> type.getCode().equals( code ) ).findFirst();
        return any.orElse( null );
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return MetadataTypeEnum
     */
    public static MetadataTypeEnum ofName( String name ) {
        try {
            return valueOf( name );
        } catch ( IllegalArgumentException e ) {
            return null;
        }
    }

}
