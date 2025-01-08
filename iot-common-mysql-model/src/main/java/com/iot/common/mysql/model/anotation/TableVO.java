package com.iot.common.mysql.model.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Orchid
 * @Create 2024/1/22
 * @Remark 扫描拥有此注解的类结构到Mybatis的类结构缓存中
 */
@Target( { ElementType.TYPE } )
@Retention( RetentionPolicy.RUNTIME )
public @interface TableVO {

}
