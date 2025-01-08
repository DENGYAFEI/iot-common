//package com.iot.common.mysql.interceptor;
//
//import cn.hutool.core.text.CharSequenceUtil;
//import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
//import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
//import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
//import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
//import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
//import com.iot.auth.context.UserContextHolder;
//import com.iot.auth.context.UserInfo;
//import com.iot.common.mysql.model.anotation.OrgPermission;
//import lombok.Builder;
//import lombok.Data;
//import lombok.SneakyThrows;
//import lombok.extern.slf4j.Slf4j;
//import net.sf.jsqlparser.expression.Alias;
//import net.sf.jsqlparser.expression.Expression;
//import net.sf.jsqlparser.expression.LongValue;
//import net.sf.jsqlparser.expression.StringValue;
//import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
//import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
//import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
//import net.sf.jsqlparser.expression.operators.relational.InExpression;
//import net.sf.jsqlparser.schema.Column;
//import net.sf.jsqlparser.schema.Table;
//import net.sf.jsqlparser.statement.select.PlainSelect;
//import net.sf.jsqlparser.statement.select.Select;
//import net.sf.jsqlparser.statement.select.SelectBody;
//import net.sf.jsqlparser.statement.select.SetOperationList;
//import org.apache.ibatis.executor.Executor;
//import org.apache.ibatis.mapping.BoundSql;
//import org.apache.ibatis.mapping.MappedStatement;
//import org.apache.ibatis.mapping.SqlCommandType;
//import org.apache.ibatis.session.ResultHandler;
//import org.apache.ibatis.session.RowBounds;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.util.Assert;
//
//import javax.annotation.Resource;
//import java.lang.reflect.Method;
//import java.sql.SQLException;
//import java.util.*;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.function.Function;
//import java.util.stream.Collectors;
//
///**
// * @Author Orchid
// * @Create 2024/04/15
// * @Remark 权限拦截器
// */
//@Slf4j
//@Component
//public class PermissionInterceptor extends JsqlParserSupport implements InnerInterceptor {
//
//    @Value( "${token.tenantId}" )
//    private String tenantId;
//
//    @Resource
//    private PermissionFeignClient permissionFeignClient;
//
//    private static final Map< String, DataPermissionCache > DATA_PERMISSION_CACHE = new ConcurrentHashMap<>();
//
//    @Data
//    @Builder
//    public static class DataPermissionCache {
//        private Boolean dataPermission;
//        private ExpressionInfo expressionInfo;
//    }
//
//    private interface CreateExpression {
//        Expression create( String leftColumn, Object value );
//    }
//
//    @Data
//    @Builder
//    public static class ExpressionInfo {
//        private String tableOrAlias;
//        private String leftColumn;
//        //  记录如何获取值
//        Function< UserInfo, Object > strategy;
//        //  记录如何用值构造表达式
//        CreateExpression expressionStrategy;
//    }
//
//    @Override
//    public void beforeQuery( Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql ) throws SQLException {
//        if ( InterceptorIgnoreHelper.willIgnoreDataPermission( ms.getId() ) ) {
//            return;
//        }
//        //  判断缓存中此接口是否需要执行数据权限
//        DataPermissionCache dataPermissionCache = DATA_PERMISSION_CACHE.get( ms.getId() );
//        if ( dataPermissionCache != null && !dataPermissionCache.getDataPermission() ) {
//            return;
//        }
//        if ( StringUtils.isBlank( tenantId ) ) {
//            log.error( "tenantId Is Blank, Please Check Your Configuration: $\\{token.tenantId\\}." );
//            return;
//        }
//        //  是否为查询语句
//        SqlCommandType sqlCommandType = ms.getSqlCommandType();
//        if ( sqlCommandType != SqlCommandType.SELECT ) {
//            return;
//        }
//        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql( boundSql );
//        mpBs.sql( this.parserSingle( mpBs.sql(), ms.getId() ) );
//    }
//
//    @Override
//    protected void processSelect( Select select, int index, String sql, Object obj ) {
//        SelectBody selectBody = select.getSelectBody();
//        if ( selectBody instanceof PlainSelect ) {
//            this.setWhere( ( PlainSelect ) selectBody, ( String ) obj );
//        } else if ( selectBody instanceof SetOperationList ) {
//            SetOperationList setOperationList = ( SetOperationList ) selectBody;
//            List< SelectBody > selectBodyList = setOperationList.getSelects();
//            selectBodyList.forEach( s -> this.setWhere( ( PlainSelect ) s, ( String ) obj ) );
//        }
//    }
//
//    private void setWhere( PlainSelect plainSelect, String whereSegment ) {
//        Expression sqlSegment = getSqlSegment( plainSelect, whereSegment );
//        if ( null != sqlSegment ) {
//            plainSelect.setWhere( sqlSegment );
//        }
//    }
//
//    @SneakyThrows( Exception.class )
//    public Expression getSqlSegment( PlainSelect plainSelect, String whereSegment ) {
//        Expression where = plainSelect.getWhere();
//        UserInfo userInfo = UserContextHolder.getUserContext().getUser();
//        if ( Objects.isNull( userInfo ) ) {
//            log.debug( "未获取到用户信息 直接跳出" );
//            return where;
//        }
//        log.info( "开始进行权限过滤,where: {},mappedStatementId: {}", where, whereSegment );
//        //  尝试从缓存中获取缓存的接口信息
//        DataPermissionCache dataPermissionCache = DATA_PERMISSION_CACHE.get( whereSegment );
//        //  获取到缓存 直接使用
//        if ( dataPermissionCache != null && dataPermissionCache.getDataPermission() ) {
//            ExpressionInfo expressionInfo = dataPermissionCache.getExpressionInfo();
//            //  获取到了缓存信息则直接使用
//            if ( Objects.nonNull( expressionInfo ) ) {
//                if ( where != null ) {
//                    return new AndExpression( where, getExpression( expressionInfo ) );
//                } else {
//                    return getExpression( expressionInfo );
//                }
//            }
//        }
//        //  未获取到缓存 重新创建
//        //  获取mapper名称
//        String className = whereSegment.substring( 0, whereSegment.lastIndexOf( "." ) );
//        //  获取方法名
//        String methodName = whereSegment.substring( whereSegment.lastIndexOf( "." ) + 1 );
//        Method[] methods = Class.forName( className ).getMethods();
//        for ( Method m : methods ) {
//            if ( Objects.equals( m.getName(), methodName ) ) {
//                OrgPermission annotation = m.getAnnotation( OrgPermission.class );
//                //  Mapper未加注解 不使用数据权限
//                if ( annotation == null ) {
//                    DATA_PERMISSION_CACHE.put( whereSegment, DataPermissionCache.builder().dataPermission( false ).build() );
//                    return where;
//                }
//                ExpressionInfo expressionInfo = getExpressionInfo( annotation );
//                if ( expressionInfo != null ) {
//                    String tableOrAlias = expressionInfo.getTableOrAlias();
//                    //  如果没有 表示使用主表的字段
//                    if ( StringUtils.isBlank( tableOrAlias ) ) {
//                        Table fromItem = ( Table ) plainSelect.getFromItem();
//                        Alias fromItemAlias = fromItem.getAlias();
//                        tableOrAlias = fromItemAlias == null ? fromItem.getName() : fromItemAlias.getName();
//                    }
//                    String leftColumn = tableOrAlias + "." + expressionInfo.getLeftColumn();
//                    expressionInfo.setLeftColumn( leftColumn );
//                    //  缓存权限
//                    DATA_PERMISSION_CACHE.put( whereSegment, DataPermissionCache.builder().dataPermission( true ).expressionInfo( expressionInfo ).build() );
//                    return where == null ? getExpression( expressionInfo ) : new AndExpression( where, getExpression( expressionInfo ) );
//                } else {
//                    EqualsTo expression = new EqualsTo();
//                    expression.setLeftExpression( new Column( "1" ) );
//                    expression.setRightExpression( new LongValue( 2 ) );
//                    return where == null ? expression : new AndExpression( where, expression );
//                }
//            }
//        }
//        return where;
//    }
//
//    private ExpressionInfo getExpressionInfo( OrgPermission annotation ) {
//        String alias = annotation.tableOrAlias();
//        String column = annotation.column();
//        return ExpressionInfo.builder()
//                .tableOrAlias( StringUtils.isNotBlank( alias ) ? alias : "" )
//                .leftColumn( StringUtils.isNotBlank( column ) ? column : "" )
//                .strategy( UserInfo::getUserId )
//                .expressionStrategy( ( c, val ) -> {
//                    R< Collection< PermissionVO< Long > > > orgR = permissionFeignClient.org( tenantId );
//                    checkResult( orgR );
//                    Optional< PermissionVO< Long > > permissionOptional = orgR.getData().stream().findFirst();
//                    Assert.isTrue( permissionOptional.isPresent(), CharSequenceUtil.format( "权限信息接口调用失败: {} -> {}", orgR.getCode(), orgR.getMessage() ) );
//                    Set< String > orgIds = permissionOptional.get().getRows().stream().map( String::valueOf ).collect( Collectors.toSet() );
//                    //  如果返回的orgIds为空 直接替换为1=2(无权限)
//                    if ( CollectionUtils.isEmpty( orgIds ) ) {
//                        log.info( "权限信息为空" );
//                        return null;
//                    }
//                    List< Expression > stringValues = new ArrayList<>();
//                    for ( String orgId : orgIds ) {
//                        stringValues.add( new StringValue( orgId ) );
//                    }
//                    InExpression inExpression = new InExpression();
//                    inExpression.setLeftExpression( new Column( c ) );
//                    inExpression.setRightItemsList( new ExpressionList( stringValues ) );
//                    return inExpression;
//                } ).build();
//    }
//
//    private Expression getExpression( ExpressionInfo expressionInfo ) {
//        CreateExpression expressionStrategy = expressionInfo.getExpressionStrategy();
//        return expressionStrategy.create( expressionInfo.getLeftColumn(), expressionInfo.getStrategy().apply( UserContextHolder.getUserContext().getUser() ) );
//    }
//
//    private void checkResult( R< Collection< PermissionVO< Long > > > orgR ) {
//        Assert.notNull( orgR, "返回结果为空" );
//        Assert.isTrue( R.OK.equals( orgR.getCode() ), orgR.getMessage() );
//        Assert.notEmpty( orgR.getData(), "权限信息为空" );
//    }
//
//}
