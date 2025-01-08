package com.iot.common.mysql.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.iot.auth.context.UserContextHolder;
import com.iot.auth.context.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @Author Orchid
 * @Create 2023/10/12
 * @Remark
 */
@Slf4j
@Component
public class MataObjectHandle implements MetaObjectHandler {

    @Override
    public void insertFill( MetaObject metaObject ) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.setValue( "createTime", now );
        metaObject.setValue( "lastUpdateTime", now );
        UserInfo userInfo = UserContextHolder.getUserContext().getUser();
        if ( userInfo == null ) {
            log.debug( "获取用户信息为空 直接跳出" );
            return;
        }
        metaObject.setValue( "creator", String.valueOf( userInfo.getUserId() ) );
        metaObject.setValue( "lastOperator", String.valueOf( userInfo.getUserId() ) );
        if ( metaObject.hasSetter( "orgId" ) ) {
            metaObject.setValue( "orgId", String.valueOf( userInfo.getOrgId() ) );
        }
    }

    @Override
    public void updateFill( MetaObject metaObject ) {
        LocalDateTime now = LocalDateTime.now();
        metaObject.setValue( "lastUpdateTime", now );
        UserInfo userInfo = UserContextHolder.getUserContext().getUser();
        if ( userInfo == null ) {
            log.debug( "获取用户信息为空 直接跳出" );
            return;
        }
        metaObject.setValue( "lastOperator", String.valueOf( userInfo.getUserId() ) );
    }

}
