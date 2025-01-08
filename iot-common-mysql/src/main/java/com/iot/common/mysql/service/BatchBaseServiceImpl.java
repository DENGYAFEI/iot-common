package com.iot.common.mysql.service;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iot.common.mysql.interfaces.BatchService;
import com.iot.common.mysql.model.pojo.entity.Base;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * @Author Orchid
 * @Create 2024/4/9
 * @Remark BatchBaseServiceImpl extends BaseServiceImpl
 */
@Transactional( rollbackFor = Exception.class )
public abstract class BatchBaseServiceImpl< M extends BaseMapper< T >, T extends Base > extends BaseServiceImpl< M, T > implements BatchService< T > {

    @Override
    public boolean batchSaveOrUpdate( List< T > entityList ) {
        return saveOrUpdateBatch( entityList );
    }

    @Override
    public boolean batchDeleteById( Collection< String > ids ) {
        return filterInvokeToBool( ids, super::removeBatchByIds );
    }

}
