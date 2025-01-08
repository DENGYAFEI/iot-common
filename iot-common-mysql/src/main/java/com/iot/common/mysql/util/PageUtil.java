package com.iot.common.mysql.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iot.common.mysql.model.pojo.dto.PageQuery;
import com.iot.common.mysql.model.pojo.dto.PageQueryDTO;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @Author Orchid
 * @Create 2024/4/13
 * @Remark
 */
public class PageUtil {

    public static < T > IPage< T > emptyPage() {
        return emptyPage( null );
    }

    public static < T > IPage< T > emptyPage( IPage< T > iPage ) {
        if ( iPage == null ) {
            return new Page<>( 1, 20, 0 );
        }
        return new Page<>( iPage.getCurrent(), iPage.getSize(), 0 );
    }

    public static < T > IPage< T > createPage( List< T > list ) {
        return createPage( null, list );
    }

    public static < T > IPage< T > createPage( IPage< ? > iPage, Class< T > clazz ) {
        return createPage( iPage, Collections.emptyList() );
    }

    public static < T > IPage< T > createPage( IPage< ? > iPage, List< T > list ) {
        if ( iPage == null ) {
            iPage = emptyPage();
        }
        Page< T > page = new Page<>( iPage.getCurrent(), iPage.getSize(), iPage.getTotal() );
        page.setRecords( Optional.ofNullable( list ).orElse( Collections.emptyList() ) );
        return page;
    }

    public static PageQuery< Object > copy( PageQuery< ? > pageQuery ) {
        return copy( pageQuery, Object.class );
    }

    public static < T > PageQuery< T > copy( PageQuery< ? > pageQuery, Class< T > targetClass ) {
        PageQuery< T > result = new PageQuery< T >( new PageQueryDTO() );
        result.setFilters( pageQuery.filters() );
        result.setOrders( pageQuery.orders() );
        result.setCondition( pageQuery.getCondition() );
        result.setOtherCondition( pageQuery.getOtherCondition() );
        result.setCurrent( pageQuery.getCurrent() );
        result.setPages( pageQuery.getPages() );
        result.setSize( pageQuery.getSize() );
        result.setTotal( pageQuery.getSize() );
        return result;
    }

}
