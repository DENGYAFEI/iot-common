package com.iot.common.excel.model.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author Orchid
 * @Create 2023/10/26
 * @Remark 导入错误异常类
 */
@Data
@ToString( callSuper = true )
@EqualsAndHashCode( callSuper = true )
public class ImportException extends RuntimeException {

    public ImportException( String message ) {
        super( message );
    }

    public ImportException( String message, Object... args ) {
        super( String.format( message, args ) );
    }

    /**
     * 唯一性校验异常
     */
    public static ImportException uniqueException( String message, Object... args ) {
        throw new ImportException( "字段唯一性校验错误: " + message, args );
    }

    /**
     * 必填项校验异常
     */
    public static ImportException required( String message, Object... args ) {
        throw new ImportException( "字段必填项校验错误: " + message, args );
    }

    /**
     * 字段长度校验异常
     */
    public static ImportException lengthException( String message, Object... args ) {
        throw new ImportException( "字段长度错误: " + message, args );
    }

    /**
     * 字段匹配错误
     */
    public static ImportException matchException( String message, Object... args ) {
        throw new ImportException( "字段匹配错误: " + message, args );
    }

    /**
     * 字段格式错误
     */
    public static ImportException formatException( String message, Object... args ) {
        throw new ImportException( "字段格式错误: " + message, args );
    }

}
