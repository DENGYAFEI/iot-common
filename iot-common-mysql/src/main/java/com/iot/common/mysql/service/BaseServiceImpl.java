package com.iot.common.mysql.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iot.common.mysql.interfaces.Service;
import com.iot.common.mysql.model.pojo.dto.PageQuery;
import com.iot.common.mysql.model.pojo.entity.Base;
import com.iot.common.base.utils.BaseUtil;
import com.iot.common.enums.EnableFlagEnum;
import com.iot.common.enums.OperationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @Author Orchid
 * @Create 2024/4/9
 * @Remark BaseServiceImpl
 */
@Slf4j
@Transactional( rollbackFor = Exception.class )
public abstract class BaseServiceImpl< M extends BaseMapper< T >, T extends Base > extends ServiceImpl< M, T > implements Service< T > {

    @Override
    public boolean add( T entity ) {
        if ( StringUtils.isBlank( entity.getId() ) ) {
            entity.setId( BaseUtil.getSnowflakeId() );
        }
        beforeOperationHandler( entity, OperationEnum.INSERT );
        boolean save = save( entity );
        afterOperationHandler( entity, OperationEnum.INSERT, save );
        return save;
    }

    @Override
    public boolean delete( String id ) {
        T entity = selectById( id );
        beforeOperationHandler( entity, OperationEnum.DELETE );
        boolean remove = removeById( id );
        afterOperationHandler( entity, OperationEnum.DELETE, remove );
        return remove;
    }

    @Override
    public boolean update( T entity ) {
        beforeOperationHandler( entity, OperationEnum.UPDATE );
        boolean update = updateById( entity );
        afterOperationHandler( entity, OperationEnum.UPDATE, update );
        return update;
    }

    @Override
    public T selectById( String id ) {
        return selectById( id, Wrappers.lambdaQuery() );
    }

    @Override
    public T selectById( String id, LambdaQueryWrapper< T > wrapper ) {
        if ( wrapper == null ) {
            wrapper = new LambdaQueryWrapper<>();
        }
        LambdaQueryWrapper< T > finalWrapper = wrapper;
        return filterInvokeToBean( id, notBlankId ->
            entityHandler( getOne( finalWrapper.eq( T::getId, notBlankId ) ) )
        );
    }

    @Override
    public List< T > selectListByIds( Collection< String > ids ) {
        return selectListByIds( ids, Wrappers.lambdaQuery() );
    }

    @Override
    public List< T > selectListByIds( Collection< String > ids, LambdaQueryWrapper< T > wrapper ) {
        if ( wrapper == null ) {
            wrapper = Wrappers.lambdaQuery();
        }
        LambdaQueryWrapper< T > finalWrapper = wrapper;
        return filterInvokeToList( ids, notBlankIds ->
            entityHandler( list( finalWrapper.in( T::getId, notBlankIds ) ) )
        );
    }

    @Override
    public IPage< T > page( PageQuery< T > pageQuery ) {
        return page( pageQuery, new LambdaQueryWrapper<>() );
    }

    @Override
    public IPage< T > page( PageQuery< T > pageQuery, Wrapper< T > wrapper ) {
        return entityHandler( super.page( pageQuery, wrapper ) );
    }

    @Override
    public List< T > selectList() {
        return selectList( Wrappers.emptyWrapper() );
    }

    @Override
    public List< T > selectList( Wrapper< T > wrapper ) {
        return super.list( wrapper );
    }

    protected void beforeOperationHandler( T entity, OperationEnum operationType ) {
        switch ( operationType ) {
            case INSERT:
                insertBeforeOperation( entity );
                break;
            case DELETE:
            case DELETE_BATCH:
                deleteBeforeOperation( entity );
                break;
            case UPDATE:
                updateBeforeOperation( entity );
                break;
        }
    }

    protected void afterOperationHandler( T entity, OperationEnum operationType, boolean operation ) {
        if ( operation ) {
            switch ( operationType ) {
                case INSERT:
                    insertAfterOperation( entity );
                    break;
                case DELETE:
                case DELETE_BATCH:
                    deleteAfterOperation( entity );
                    break;
                case UPDATE:
                    updateAfterOperation( entity );
                    break;
            }
        }
    }

    protected void beforeBatchOperationHandler( Collection< T > entityList, OperationEnum operationType ) {
        for ( T entity : entityList ) {
            beforeOperationHandler( entity, operationType );
        }
    }

    protected void afterBatchOperationHandler( Collection< T > entityList, OperationEnum operationType, boolean operation ) {
        for ( T entity : entityList ) {
            afterOperationHandler( entity, operationType, operation );
        }
    }

    protected void insertBeforeOperation( T entity ) {

    }

    protected void deleteBeforeOperation( T entity ) {

    }

    protected void updateBeforeOperation( T entity ) {

    }

    protected void insertAfterOperation( T entity ) {

    }

    protected void deleteAfterOperation( T entity ) {

    }

    protected void updateAfterOperation( T entity ) {

    }

    /**
     * 实体查询后置处理
     */
    protected < E extends T > E entityHandler( E entity ) {
        return entity;
    }

    /**
     * 实体查询后置处理
     */
    protected < E extends T > List< E > entityHandler( List< E > entityList ) {
        if ( CollectionUtils.isEmpty( entityList ) ) {
            return entityList;
        }
        return entityList.stream().map( this::entityHandler ).collect( Collectors.toList() );
    }

    /**
     * 实体查询后置处理
     */
    protected < E extends T > IPage< E > entityHandler( IPage< E > entityPage ) {
        if ( entityPage == null ) {
            return null;
        }
        List< E > records = entityPage.getRecords();
        entityPage.setRecords( entityHandler( records ) );
        return entityPage;
    }

    /**
     * 过滤空的Collection(String)
     *
     * @param collection collection
     * @return 返回collection中所有不为空字符串的collection
     */
    public Collection< String > filterCollection( Collection< String > collection ) {
        return filterCollection( collection, false );
    }

    /**
     * 过滤空的Collection(String)
     *
     * @param collection      collection
     * @param addEmptyElement 当集合为空的时候 返回结果是否增加一个空字符串
     * @return 返回collection中所有不为空字符串的collection
     */
    public Collection< String > filterCollection( Collection< String > collection, boolean addEmptyElement ) {
        Collection< String > result;
        result = collection == null ? new ArrayList<>() : collection;
        if ( result.isEmpty() ) {
            if ( addEmptyElement ) {
                result.add( "" );
            }
            return result;
        }
        return result.stream().filter( StringUtils::isNotBlank ).collect( Collectors.toCollection( ArrayList::new ) );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回一个emptyMap
     *
     * @param collection collection
     * @param producer   返回执行的策略
     */
    public Map< String, T > filterInvokeToMap( Collection< String > collection, Function< Collection< String >, Map< String, T > > producer ) {
        return filterInvokeToMap( collection, producer, Collections::emptyMap );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回reject提供的值
     *
     * @param collection collection
     * @param producer   返回执行的策略
     * @param reject     当collection为空的时候返回的拒绝策略
     */
    public Map< String, T > filterInvokeToMap( Collection< String > collection, Function< Collection< String >, Map< String, T > > producer, Supplier< Map< String, T > > reject ) {
        collection = filterCollection( collection );
        log.info( "collection size: {}", collection.size() );
        if ( collection.isEmpty() ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( filterCollection( collection ) );
    }

    /**
     * 如果提供的String为空 将会返回一个emptyMap
     *
     * @param str      str
     * @param producer 返回执行的策略
     */
    public Map< String, T > filterInvokeToMap( String str, Function< String, Map< String, T > > producer ) {
        return filterInvokeToMap( str, producer, Collections::emptyMap );
    }

    /**
     * 如果提供的String为空 将会返回reject提供的值
     *
     * @param str      str
     * @param producer 返回执行的策略
     * @param reject   当String为空的时候返回的拒绝策略
     */
    public Map< String, T > filterInvokeToMap( String str, Function< String, Map< String, T > > producer, Supplier< Map< String, T > > reject ) {
        log.info( "String : {}", str );
        if ( StringUtils.isBlank( str ) ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( str.trim() );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回一个emptyList
     *
     * @param collection collection
     * @param producer   返回执行的策略
     */
    public List< T > filterInvokeToList( Collection< String > collection, Function< Collection< String >, List< T > > producer ) {
        return filterInvokeToList( collection, producer, Collections::emptyList );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回reject提供的值
     *
     * @param collection collection
     * @param producer   返回执行的策略
     * @param reject     当collection为空的时候返回的拒绝策略
     */
    public List< T > filterInvokeToList( Collection< String > collection, Function< Collection< String >, List< T > > producer, Supplier< List< T > > reject ) {
        collection = filterCollection( collection );
        log.info( "collection size: {}", collection.size() );
        if ( collection.isEmpty() ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( collection );
    }

    /**
     * 如果提供的String为空 将会返回一个emptyList
     *
     * @param str      str
     * @param producer 返回执行的策略
     */
    public List< T > filterInvokeToList( String str, Function< String, List< T > > producer ) {
        return filterInvokeToList( str, producer, Collections::emptyList );
    }

    /**
     * 如果提供的String为空 将会返回reject提供的值
     *
     * @param str      str
     * @param producer 返回执行的策略
     * @param reject   当collection为空的时候返回的拒绝策略
     */
    public List< T > filterInvokeToList( String str, Function< String, List< T > > producer, Supplier< List< T > > reject ) {
        log.info( "String : {}", str );
        if ( StringUtils.isBlank( str ) ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( str.trim() );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回Null
     *
     * @param str      str
     * @param producer 返回执行的策略
     */
    public T filterInvokeToBean( String str, Function< String, T > producer ) {
        return filterInvokeToBean( str, producer, () -> null );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回reject提供的值
     *
     * @param str      str
     * @param producer 返回执行的策略
     * @param reject   当collection为空的时候返回的拒绝策略
     */
    public T filterInvokeToBean( String str, Function< String, T > producer, Supplier< T > reject ) {
        log.info( "String : {}", str );
        if ( StringUtils.isBlank( str ) ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( str.trim() );
    }

    /**
     * 如果提供的String为空 将会返回true
     *
     * @param str      str
     * @param producer 返回执行的策略
     */
    public boolean filterInvokeToBool( String str, Function< String, Boolean > producer ) {
        return filterInvokeToBool( str, producer, () -> Boolean.TRUE );
    }

    /**
     * 如果提供的String为空 将会返回reject提供的值
     *
     * @param str      str
     * @param producer 返回执行的策略
     * @param reject   当collection为空的时候返回的拒绝策略
     */
    public boolean filterInvokeToBool( String str, Function< String, Boolean > producer, Supplier< Boolean > reject ) {
        log.info( "String : {}", str );
        if ( StringUtils.isBlank( str ) ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( str.trim() );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回true
     *
     * @param collection collection
     * @param producer   返回执行的策略
     */
    public boolean filterInvokeToBool( Collection< String > collection, Function< Collection< String >, Boolean > producer ) {
        return filterInvokeToBool( collection, producer, () -> Boolean.TRUE );
    }

    /**
     * 如果提供的collection为空或者所有元素都是空 将会返回reject提供的值
     *
     * @param collection collection
     * @param producer   返回执行的策略
     * @param reject     当collection为空的时候返回的拒绝策略
     */
    public boolean filterInvokeToBool( Collection< String > collection, Function< Collection< String >, Boolean > producer, Supplier< Boolean > reject ) {
        collection = filterCollection( collection );
        log.info( "collection size: {}", collection.size() );
        if ( collection.isEmpty() ) {
            log.info( "reject" );
            return reject.get();
        }
        log.info( "producer" );
        return producer.apply( collection );
    }

    public LambdaQueryWrapper< T > enableWrapper( LambdaQueryWrapper< T > wrapper, SFunction< T, EnableFlagEnum > func, Boolean enable ) {
        if ( enable != null ) {
            return wrapper.eq( func, Boolean.TRUE.equals( enable ) ? EnableFlagEnum.ENABLE : EnableFlagEnum.DISABLE );
        }
        return wrapper;
    }

    public LambdaQueryChainWrapper< T > enableWrapper( LambdaQueryChainWrapper< T > wrapper, SFunction< T, EnableFlagEnum > func, Boolean enable ) {
        if ( enable != null ) {
            return wrapper.eq( func, Boolean.TRUE.equals( enable ) ? EnableFlagEnum.ENABLE.getIndex() : EnableFlagEnum.DISABLE.getIndex() );
        }
        return wrapper;
    }

}
