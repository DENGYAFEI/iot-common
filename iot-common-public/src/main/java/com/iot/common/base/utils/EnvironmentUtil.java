package com.iot.common.base.utils;

import cn.hutool.core.util.RandomUtil;
import com.iot.common.constant.EnvironmentConstant;
import com.iot.common.constant.ExceptionConstant;
import com.iot.common.constant.SymbolConstant;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark
 */
public class EnvironmentUtil {

    private EnvironmentUtil() {
        throw new IllegalStateException( ExceptionConstant.UTILITY_CLASS );
    }

    /**
     * 获取节点 ID
     *
     * @return String Suffix
     */
    public static String getNodeId() {
        return RandomUtil.randomString( 8 );
    }

    /**
     * 获取 Topic Tag
     * 开发环境用于区分多人开发
     *
     * @param env   环境类型
     * @param group 分组
     * @return String Tag
     */
    public static String getTag( String env, String group ) {
        String exchangeTag = "";
        if ( isDev( env ) ) {
            exchangeTag = env.toLowerCase() + SymbolConstant.UNDERSCORE + group.toLowerCase() + SymbolConstant.DOT;
        }
        return exchangeTag;
    }

    /**
     * 是否为开发环境
     *
     * @param env 环境类型
     * @return 是/否
     */
    public static boolean isDev( String env ) {
        return EnvironmentConstant.ENV_DEV.equals( env );
    }

}
