package com.iot.common.base.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

/**
 * @Author Orchid
 * @Create 2024/6/1
 * @Remark 类型工具类
 */
public class TypeUtil {

    private static final Class< ? >[] fundamentalTypes = new Class< ? >[]{
        Integer.class, String.class, Character.class,
        Float.class, Double.class, Long.class,
        Short.class, Byte.class, Boolean.class,
        int.class, char.class,
        float.class, double.class, long.class,
        short.class, byte.class, boolean.class
    };

    private static final Class< ? >[] dateTypes = new Class< ? >[]{
        Date.class, LocalDate.class
    };
    private static final Class< ? >[] datetimeTypes = new Class< ? >[]{
        Date.class, LocalDateTime.class
    };

    /**
     * 判断对应类型是否为基本类型
     */
    public static boolean isFundamentalType( Class< ? > clazz ) {
        if ( clazz == null ) {
            return false;
        }
        return Arrays.stream( fundamentalTypes ).anyMatch( c -> c == clazz );
    }

    /**
     * 判断对应对象是否为基本类型对象
     */
    public static boolean isFundamentalType( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        return isFundamentalType( obj.getClass() );
    }

    /**
     * 判断对应类型是否为日期类型
     */
    public static boolean isDateType( Class< ? > clazz ) {
        if ( clazz == null ) {
            return false;
        }
        return Arrays.stream( dateTypes ).anyMatch( c -> c == clazz );
    }

    /**
     * 判断对应对象是否为日期类型对象
     */
    public static boolean isDateType( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        return isDateType( obj.getClass() );
    }

    /**
     * 判断对应类型是否为日期时间类型
     */
    public static boolean isDateTimeType( Class< ? > clazz ) {
        if ( clazz == null ) {
            return false;
        }
        return Arrays.stream( datetimeTypes ).anyMatch( c -> c == clazz );
    }

    /**
     * 判断对应对象是否为日时间期类型对象
     */
    public static boolean isDateTimeType( Object obj ) {
        if ( obj == null ) {
            return false;
        }
        return isDateTimeType( obj.getClass() );
    }

    /**
     * 判断对应类型是否为日期或日期时间类型
     */
    public static boolean isDateOrDateTimeType( Class< ? > clazz ) {
        return isDateType( clazz ) || isDateTimeType( clazz );
    }

    /**
     * 判断对应对象是否为日期或日期时间期类型对象
     */
    public static boolean isDateOrDateTimeType( Object obj ) {
        return isDateType( obj ) || isDateTimeType( obj );
    }

    /**
     * 将一个String解析为需要的类型 不支持的类型将会返回null
     *
     * @param type  目标类型
     * @param value 待解析值
     */
    public static Object resolverStringValue( Class< ? > type, String value ) {
        return resolverStringValue( type, value, false );
    }

    /**
     * 将一个String解析为需要的类型
     *
     * @param type           目标类型
     * @param value          待解析值
     * @param throwException 不支持的类型 false 返回null true 抛出异常
     */
    public static Object resolverStringValue( Class< ? > type, String value, boolean throwException ) {
        if ( value == null || type == null || Void.class.equals( type ) ) {
            return null;
        }
        //  fundamentalTypes is final
        if ( String.class.equals( type ) ) {
            return value;
        }
        try {
            if ( Integer.class.equals( type ) || int.class.equals( type ) ) {
                return Integer.parseInt( value );
            }
            if ( Boolean.class.equals( type ) || boolean.class.equals( type ) ) {
                return "1".equals( value ) || Boolean.parseBoolean( value );
            }
            if ( Long.class.equals( type ) || long.class.equals( type ) ) {
                return Long.parseLong( value );
            }
            if ( Float.class.equals( type ) || float.class.equals( type ) ) {
                return Float.valueOf( value );
            }
            if ( Double.class.equals( type ) || double.class.equals( type ) ) {
                return Double.parseDouble( value );
            }
            if ( Short.class.equals( type ) || short.class.equals( type ) ) {
                return Short.parseShort( value );
            }
            if ( Byte.class.equals( type ) || byte.class.equals( type ) ) {
                return Byte.valueOf( value );
            }
            if ( Character.class.equals( type ) || char.class.equals( type ) ) {
                return value.length() < 1 ? ' ' : value.charAt( 0 );
            }
        }catch ( Exception e ){
            return null;
        }
        if ( throwException ) {
            throw new RuntimeException( "不支持的类型: " + type.getName() );
        }
        return null;
    }

    /**
     * 将一个Object解析为需要的类型 不支持的类型将会返回null
     *
     * @param type  目标类型
     * @param value 待解析值
     */
    public static Object resolverValue( Class< ? > type, Object value ) {
        return resolverValue( type, value, false );
    }

    /**
     * 将一个Object解析为需要的类型
     *
     * @param type           目标类型
     * @param value          待解析值
     * @param throwException 不支持的类型 false 返回null true 抛出异常
     */
    public static Object resolverValue( Class< ? > type, Object value, boolean throwException ) {
        if ( value == null || type == null || Void.class.equals( type ) ) {
            return null;
        }
        Class< ? > valueClass = value.getClass();
        //  判断valueClass是否为type的子类
        if ( type.isAssignableFrom( valueClass ) ) {
            return value;
        }
        return resolverStringValue( type, value.toString(), throwException );
    }


}
