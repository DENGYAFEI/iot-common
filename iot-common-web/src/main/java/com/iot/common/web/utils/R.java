package com.iot.common.web.utils;


import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/* 返回信息 */
@Getter
@Setter
@AllArgsConstructor
public class R< T > {

    private static Integer OK = 0;

    private static Integer FAILED = 1;

    private R() {

    }

    /* 状态码 */
    private Integer code;

    /* 提示信息 */
    private String message = "";

    /* 封装结果集 */
    private T data;

    /* 查询到的总数 */
    private long total = 0;

    @JsonIgnore
    public boolean isSuccess(){
        return R.OK.equals( code );
    }

    public static < T > R< T > success() {
        return success( null, "成功" );
    }

    public static < T > R< T > success( T data ) {
        return success( data, "" );
    }

    public static < T > R< T > success( T data, String message ) {
        return success( data, message, 0L );
    }

    public static < T > R< T > success( T data, Long total ) {
        return success( data, "", total );
    }

    public static < T > R< T > success( T data, String message, Long total ) {
        return new R<>( OK, message, data, total );
    }

    public static < T > R< List< T > > page( IPage< T > page ) {
        return new R<>( OK, "", page.getRecords(), page.getTotal() );
    }

    // public static R successImport( long rowCount ) {
    //     if ( rowCount < 0 ) {
    //         throw new IllegalArgumentException( String.valueOf( rowCount ) );
    //     }
    //     return success( String.format( "成功导入%d条数据", rowCount ) );
    // }

    public static < T > R< T > failed() {
        return R.failed( "失败" );
    }

    public static < T > R< T > failed( String message ) {
        return new R<>( FAILED, message, null, 0L );
    }

    public static < T > R< T > custom( int code, String message ) {
        return new R<>( code, message, null, 0L );
    }

    public static < T > R< T > custom( int code, String message, T data, int total ) {
        return new R<>( code, message, data, total );
    }

    public static < T > R< T > bool( boolean success ) {
        return success ? R.success() : R.failed();
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr( this );
    }
}
