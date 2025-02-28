package com.iot.common.base.utils;

import cn.hutool.crypto.digest.MD5;
import com.iot.common.constant.ExceptionConstant;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark 编码工具类
 */
@Slf4j
public class DecodeUtil {

    private DecodeUtil() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 字节转字符串
     * <p>
     * UTF-8
     *
     * @param bytes 字节数组
     * @return 字符串
     */
    public static String byteToString( byte[] bytes ) {
        return new String( bytes, StandardCharsets.UTF_8 );
    }

    /**
     * 字符串转字节
     * <p>
     * UTF-8
     *
     * @param content 字符串
     * @return 字节数组
     */
    public static byte[] stringToByte( String content ) {
        return content.getBytes( StandardCharsets.UTF_8 );
    }

    /**
     * 获取 MD5 编码
     *
     * @param content 字符串
     * @return MD5 字符串
     */
    public static String md5( String content ) {
        MD5 md5 = MD5.create();
        return md5.digestHex( content, StandardCharsets.UTF_8 );
    }

    /**
     * 获取 MD5 编码
     *
     * @param content 字符串
     * @param salt    盐值
     * @return MD5 字符串
     */
    public static String md5( String content, String salt ) {
        return md5( md5( content ) + salt );
    }

    /**
     * 将字节流进行Base64编码
     *
     * @param bytes Byte Array
     * @return Byte Array
     */
    public static byte[] encode( byte[] bytes ) {
        return Base64.getEncoder().encode( bytes );
    }

    /**
     * 将字符串进行Base64编码
     *
     * @param content 字符串
     * @return Byte Array
     */
    public static byte[] encode( String content ) {
        return encode( stringToByte( content ) );
    }

    /**
     * 必须配合encode使用，用于encode编码之后解码
     *
     * @param bytes Byte Array
     * @return Byte Array
     */
    public static byte[] decode( byte[] bytes ) {
        return Base64.getDecoder().decode( bytes );
    }

    /**
     * 必须配合encode使用，用于encode编码之后解码
     *
     * @param content 字符串
     * @return Byte Array
     */
    public static byte[] decode( String content ) {
        return decode( stringToByte( content ) );
    }

}
