package com.iot.common.mysql.interceptor;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.plugins.InterceptorIgnoreHelper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.ExceptionUtils;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.iot.common.mysql.model.pojo.dto.PageQuery;
import com.iot.common.mysql.expression.ChainOrExpression;
import com.iot.common.mysql.model.pojo.dto.FilterItem;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author Orchid
 * @Create 2024/4/2
 * @Remark
 */
@Slf4j
@Component
public class QueryFilterInterceptor implements InnerInterceptor {

    public static Map< Class< ? >, Set< String > > cacheMap = new ConcurrentHashMap<>( 256 );

    @Override
    public void beforeQuery( Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql ) throws SQLException {
        if ( InterceptorIgnoreHelper.willIgnoreDataPermission( ms.getId() ) ) {
            return;
        }
        SqlCommandType sqlCommandType = ms.getSqlCommandType();
        if ( sqlCommandType != SqlCommandType.SELECT ) {
            return;
        }
        Object parameterObject = boundSql.getParameterObject();
        PageQuery< ? > pageQuery = getPageQuery( parameterObject );
        if ( pageQuery == null ) {
            return;
        }

        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql( boundSql );
        mpBs.sql( this.parserSingle( mpBs.sql(), pageQuery, ms ) );
    }

    private String parserSingle( String sql, PageQuery< ? > pageQuery, MappedStatement ms ) {
        if ( log.isDebugEnabled() ) {
            log.debug( "original SQL: " + sql );
        }
        try {
            Statement statement = CCJSqlParserUtil.parse( sql );
            return processSelect( ( Select ) statement, pageQuery, ms );
        } catch ( JSQLParserException e ) {
            throw ExceptionUtils.mpe( "Failed to process, Error SQL: %s", e.getCause(), sql );
        }
    }

    private String processSelect( Select select, PageQuery< ? > pageQuery, MappedStatement ms ) {
        SelectBody selectBody = select.getSelectBody();
        if ( selectBody instanceof PlainSelect ) {
            this.setWhere( ( PlainSelect ) selectBody, pageQuery, ms );
        } else if ( selectBody instanceof SetOperationList ) {
            SetOperationList setOperationList = ( SetOperationList ) selectBody;
            List< SelectBody > selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach( s -> this.setWhere( ( PlainSelect ) s, pageQuery, ms ) );
        }
        return select.toString();
    }


    private void setWhere( PlainSelect plainSelect, PageQuery< ? > pageQuery, MappedStatement ms ) {
        Expression sqlSegment = getSqlSegment( plainSelect, pageQuery, ms );
        if ( null != sqlSegment ) {
            plainSelect.setWhere( sqlSegment );
        }
    }

    @SneakyThrows( Exception.class )
    private Expression getSqlSegment( PlainSelect plainSelect, PageQuery< ? > pageQuery, MappedStatement ms ) {

        Class< ? > type = ms.getResultMaps().get( 0 ).getType();
        Set< String > fieldSet = cacheMap.get( type );
        if ( fieldSet == null ) {
            List< Field > beanFields = TableInfoHelper.getAllFields( type );
            fieldSet = beanFields.stream().map( Field::getName ).collect( Collectors.toSet() );
            Class< ? > superclass = type.getSuperclass();
            // if ( superclass == BaseEntity.class ) {
            //     Field[] declaredFields = superclass.getDeclaredFields();
            //     fieldSet.addAll( Arrays.stream( declaredFields ).map( Field::getName ).collect( Collectors.toSet() ) );
            // }
            cacheMap.put( type, fieldSet );
        }

        // 检查Filter参数
        checkFilterParams( pageQuery, fieldSet );
        // 检查Condition参数
        checkAndParseConditionParams( pageQuery, fieldSet );

        return concat( plainSelect, pageQuery );
    }


    private void checkFilterParams( PageQuery< ? > pageQuery, Set< String > fieldSet ) {
        if ( pageQuery == null ) return;
        List< FilterItem > filters = pageQuery.filters();
        if ( filters != null ) {
            for ( FilterItem filter : filters ) {
                if ( !fieldSet.contains( filter.getColumn() ) ) {
                    // throw new AppException(-1, "参数错误");
                    throw new RuntimeException( "参数错误" );
                }
            }
        }
    }


    private void checkAndParseConditionParams( PageQuery< ? > pageQuery, Set< String > fieldSet ) {
        if ( pageQuery == null ) return;
        Map< String, Object > condition = pageQuery.getCondition();
        if ( condition != null && condition.size() > 0 ) {
            Map< String, Object > newCondition = new LinkedHashMap<>();

            Set< Map.Entry< String, Object > > entries = condition.entrySet();

            for ( Map.Entry< String, Object > entry : entries ) {
                if ( fieldSet.contains( entry.getKey() ) ) {
                    newCondition.put( entry.getKey(), entry.getValue() );
                }
            }
            pageQuery.setCondition( newCondition );
        }

    }


    private Expression concat( PlainSelect plainSelect, PageQuery< ? > pageQuery ) {
        if ( CollectionUtils.isNotEmpty( pageQuery.filters() ) || pageQuery.getCondition() != null ) {
            try {
                List< FilterItem > filterList = pageQuery.filters();
                Map< String, Object > condition = pageQuery.getCondition();

                List< SelectItem > selectItems = plainSelect.getSelectItems();
                Map< String, SelectItem > selectItemMap = selectItems.stream().collect( Collectors.toMap( selectItem -> {
                            SelectExpressionItem selectExpressionItem = ( SelectExpressionItem ) selectItem;
                            if ( selectExpressionItem.getAlias() != null ) {
                                /**
                                 * 别名当key
                                 */
                                return selectExpressionItem.getAlias().getName().toUpperCase();
                            } else {
                                return selectExpressionItem.getASTNode().jjtGetLastToken().image.toUpperCase();
                            }
                        }
                        , Function.identity()
                ) );


                Expression where = plainSelect.getWhere();

                Expression whereReturn = addFilters( filterList, selectItemMap, where );
                whereReturn = addCondition( condition, selectItemMap, whereReturn );

                return whereReturn;
            } catch ( Exception e ) {
                e.printStackTrace();
                log.error( e.getMessage() );
            }
        }
        return plainSelect.getWhere();
    }

    private Expression addFilters( List< FilterItem > filterList, Map< String, SelectItem > selectItemMap, Expression where ) {
        if ( filterList == null ) return where;
        Expression exp = where;
        for ( FilterItem item : filterList ) {
            String type = item.getType();
            if ( exp == null ) {
                exp = getExpression( item, selectItemMap );
            } else if ( "or".equals( type ) ) {
                exp = new OrExpression( exp, getExpression( item, selectItemMap ) );
            } else {
                exp = new AndExpression( exp, getExpression( item, selectItemMap ) );
            }
        }

        return exp;
    }

    private Expression addCondition( Map< String, Object > condition, Map< String, SelectItem > selectItemMap, Expression where ) {
        if ( condition == null ) return where;
        Expression exp = where;
        for ( Map.Entry< String, Object > entry : condition.entrySet() ) {

            String column = StrUtil.toUnderlineCase( entry.getKey() ).toUpperCase();
            SelectItem selectItem = selectItemMap.get( column );
            SelectExpressionItem selectExpressionItem = ( SelectExpressionItem ) selectItem;

            Column expression = ( Column ) selectExpressionItem.getExpression();
            column = expression.getName( true );

            EqualsTo equalsTo = new EqualsTo();
            equalsTo.setLeftExpression( new Column( column ) );
            equalsTo.setRightExpression( new StringValue( String.valueOf( entry.getValue() ) ) );
            if ( exp == null ) {
                exp = equalsTo;
            } else {
                exp = new AndExpression( exp, equalsTo );
            }
        }
        return exp;
    }


    private Expression getExpression( FilterItem filterItem, Map< String, SelectItem > selectItemMap ) {
        String operator = filterItem.getOperator();
        String column = StrUtil.toUnderlineCase( filterItem.getColumn() ).toUpperCase();
        String value = filterItem.getValue();

        SelectItem selectItem = selectItemMap.get( column );
        SelectExpressionItem selectExpressionItem = ( SelectExpressionItem ) selectItem;

        Column expression = ( Column ) selectExpressionItem.getExpression();
        column = expression.getName( true );

        Expression exp = null;
        if ( "=".equals( operator ) ) {
            return getExpression( operator, column, value );
        }
        value = value.replaceAll( "，", "," );
        if ( "between".equals( operator ) ) {
            Between between = new Between();
            String[] split = value.split( "," );
            between.setLeftExpression( new Column( column ) );

            between.setBetweenExpressionStart( new StringValue( split[0] ) );
            between.setBetweenExpressionEnd( new StringValue( split[1] ) );

            exp = between;
        } else if ( "%like%".contains( operator ) ) {
            String[] split = value.split( "," );
            List< Expression > orList = new ArrayList<>();
            for ( String keyword : split ) {
                orList.add( getExpression( operator, column, keyword ) );
            }
            exp = new ChainOrExpression( orList );

        } else if ( "in not in".contains( operator ) ) {
            String[] split = value.split( "," );
            List< Expression > valList = new ArrayList<>();

            for ( String keyword : split ) {
                valList.add( new StringValue( keyword ) );
            }

            InExpression inExpression = new InExpression();
            if ( "not in".equals( operator ) ) {
                inExpression.setNot( true );
            }

            inExpression.setLeftExpression( new Column( column ) );
            ExpressionList expressionList = new ExpressionList();
            expressionList.setExpressions( valList );
            inExpression.setRightItemsList( expressionList );
            exp = inExpression;
        } else {
            exp = getExpression( operator, column, value );
        }

        return exp;
    }

    private Expression getExpression( String operator, String column, String value ) {
        BinaryExpression exp = null;
        if ( "=".equals( operator ) ) {
            exp = new EqualsTo();
        } else if ( ">".equals( operator ) ) {
            exp = new GreaterThan();
        } else if ( ">=".equals( operator ) ) {
            exp = new GreaterThanEquals();
        } else if ( "<".equals( operator ) ) {
            exp = new MinorThan();
        } else if ( "<=".equals( operator ) ) {
            exp = new MinorThanEquals();
        } else if ( "like%".equals( operator ) ) {
            exp = new LikeExpression();
            value = value + "%";
        } else if ( "%like".equals( operator ) ) {
            exp = new LikeExpression();
            value = "%" + value;
        } else if ( "like".equals( operator ) ) {
            exp = new LikeExpression();
            value = "%" + value + "%";
        }

        if ( exp != null ) {
            exp.setLeftExpression( new Column( column ) );
            exp.setRightExpression( new StringValue( value ) );
        }
        return exp;
    }

    private PageQuery< ? > getPageQuery( Object paramObj ) {
        PageQuery< ? > pageQuery = null;
        if ( paramObj instanceof PageQuery ) {
            pageQuery = ( PageQuery ) paramObj;
        } else if ( paramObj instanceof Map ) {
            for ( Object arg : ( ( Map ) paramObj ).values() ) {
                if ( arg instanceof PageQuery ) {
                    pageQuery = ( PageQuery ) arg;
                    break;
                }
            }
        }
        return pageQuery;
    }

}
