package com.iot.common.mysql.expression;

import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;

import java.util.List;
import java.util.StringJoiner;

/**
 * @创建人 zsy
 * @创建时间 2023-02-24
 * @描述 没有描述
 */
public class ChainOrExpression extends BinaryExpression {

    private final List< Expression > expressionList;

    public ChainOrExpression( List< Expression > expressionList ) {
        this.expressionList = expressionList;
    }

    @Override
    public String getStringExpression() {
        return "";
    }

    @Override
    public void accept( ExpressionVisitor expressionVisitor ) {
        for ( Expression expression : expressionList ) {
            expressionVisitor.visit( ( LikeExpression ) expression );
        }
    }

    @Override
    public String toString() {
        StringJoiner stringJoiner = new StringJoiner( " OR " );
        for ( Expression expression : expressionList ) {
            stringJoiner.add( expression.toString() );
        }
        return "(" + stringJoiner + ")";
    }
}
