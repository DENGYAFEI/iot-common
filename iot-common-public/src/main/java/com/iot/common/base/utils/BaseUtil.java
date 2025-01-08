package com.iot.common.base.utils;

import cn.hutool.core.util.IdUtil;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * @Author Orchid
 * @Create 2024/4/9
 * @Remark Base工具
 */
public class BaseUtil {

    private static final String numbers = "0123456789";

    /**
     * 生成ID
     */
    public static String getSnowflakeId() {
        return IdUtil.getSnowflakeNextIdStr();
    }

    /**
     * @param len 需要的长度
     * @return 生成随机的数字
     */
    public static String getRandomNumber( int len ) {
        StringBuilder sb = new StringBuilder( len );
        Random random = new Random();
        for ( int i = 0; i < len; i++ ) {
            int randomIndex = random.nextInt( numbers.length() );
            char randomChar = numbers.charAt( randomIndex );
            sb.append( randomChar );
        }
        return sb.toString();
    }

    /**
     * @return 根据当前时间生成一个唯一编码 (时间戳+四位随机数)
     */
    private static String getTimeCoding() {
        LocalDateTime now = LocalDateTime.now( ZoneOffset.of( "+8" ) );
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyMMddHHmmssSSS" );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern( "yyMMddHHmmss" );
        return formatter.format( now ) + getRandomNumber( 4 );
    }

    /**
     * 生成一个设备唯一编码 (时间戳+四位随机数)
     */
    public static String getDeviceTimeCoding() {
        return "D" + getTimeCoding();
    }

    /**
     * 生成一个点位唯一编码 (时间戳+四位随机数)
     */
    public static String getPointTimeCoding() {
        return "P" + getTimeCoding();
    }

    /**
     * 生成一个模板唯一编码 (时间戳+四位随机数)
     */
    public static String getProfileTimeCoding() {
        return "T" + getTimeCoding();
    }

    /**
     * 生成一个采集唯一编码 (时间戳+四位随机数)
     */
    public static String getGatherTimeCoding() {
        return "G" + getTimeCoding();
    }

}
