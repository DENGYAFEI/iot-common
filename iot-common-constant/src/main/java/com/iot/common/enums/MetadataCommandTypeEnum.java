package com.iot.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

/**
 * @Author Orchid
 * @Create 2024/4/3
 * @Remark 通用元数据指令类型枚举
 */
@Getter
@AllArgsConstructor
public enum MetadataCommandTypeEnum {

    /**
     * 新增
     */
    ADD( ( byte ) 0x00, "add", "新增" ),

    /**
     * 删除
     */
    DELETE( ( byte ) 0x01, "delete", "删除" ),

    /**
     * 修改
     */
    UPDATE( ( byte ) 0x02, "update", "修改" ),
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
     * @return MetadataCommandTypeEnum
     */
    public static MetadataCommandTypeEnum ofCode( String code ) {
        Optional< MetadataCommandTypeEnum > any = Arrays.stream( MetadataCommandTypeEnum.values() ).filter( type -> type.getCode().equals( code ) ).findFirst();
        return any.orElse( null );
    }

    /**
     * 根据 Name 获取枚举
     *
     * @param name Name
     * @return MetadataCommandTypeEnum
     */
    public static MetadataCommandTypeEnum ofName( String name ) {
        try {
            return valueOf( name );
        } catch ( IllegalArgumentException e ) {
            return null;
        }
    }

}
