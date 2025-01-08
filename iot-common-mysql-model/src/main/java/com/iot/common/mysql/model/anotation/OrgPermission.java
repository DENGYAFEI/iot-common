package com.iot.common.mysql.model.anotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author Orchid
 * @Create 2024/4/15
 * @Remark 权限注解
 */
@Target( { ElementType.METHOD } )
@Retention( RetentionPolicy.RUNTIME )
public @interface OrgPermission {

    String tableOrAlias() default "";

    String column() default "org_id";

}
