package com.iot.common.mysql.interfaces;

import com.iot.common.mysql.model.pojo.entity.Base;

import java.util.Collection;
import java.util.List;

/**
 * @Author Orchid
 * @Create 2024/4/9
 * @Remark 批量服务接口
 */
public interface BatchService< T extends Base > extends Service< T > {

    /**
     * 批量新增或更新
     */
    boolean batchSaveOrUpdate( List< T > entityList );

    /**
     * 根据ID批量删除
     */
    boolean batchDeleteById( Collection< String > ids );

}
