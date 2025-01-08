package com.iot.common.mysql.interfaces;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iot.common.mysql.model.pojo.dto.PageQuery;
import com.iot.common.mysql.model.pojo.entity.Base;

import java.util.Collection;
import java.util.List;

/**
 * @Author Orchid
 * @Create 2024/4/9
 * @Remark 基础 Service 类接口
 */
public interface Service< T extends Base > {

    /**
     * 新增
     */
    boolean add( T entity );

    /**
     * 根据ID删除
     */
    boolean delete( String id );

    /**
     * 更新
     */
    boolean update( T entity );

    /**
     * 通过 ID 查询
     */
    T selectById( String id );

    /**
     * 通过 ID 查询
     */
    T selectById( String id, LambdaQueryWrapper< T > wrapper );

    /**
     * 根据ID查询列表
     */
    List< T > selectListByIds( Collection< String > ids );

    /**
     * 根据ID查询列表
     */
    List< T > selectListByIds( Collection< String > ids, LambdaQueryWrapper< T > wrapper );

    /**
     * 获取带分页、排序
     */
    IPage< T > page( PageQuery< T > pageQuery );

    /**
     * 获取带分页、排序
     */
    IPage< T > page( PageQuery< T > pageQuery, Wrapper< T > wrapper );

    /**
     * 查询列表
     */
    List< T > selectList();

    /**
     * 根据条件查询列表
     */
    List< T > selectList( Wrapper< T > wrapper );

}
