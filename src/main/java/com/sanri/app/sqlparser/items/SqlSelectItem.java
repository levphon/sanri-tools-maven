package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;

public class SqlSelectItem implements ParserItem  {
    private SelectItem selectItem;

    private SelectItemType selectItemType;
    private Alias alias;
    private String expression;
    private SqlSelectBody sqlSelectBody;

    public SqlSelectItem(SelectItem selectItem) {
        this.selectItem = selectItem;
    }

    public void parser() {
        if(selectItem instanceof SelectExpressionItem){
            parserExpressionItem((SelectExpressionItem) selectItem);
        }else if(selectItem instanceof AllTableColumns){
            parserAllTableColumns((AllTableColumns) selectItem);
        }else if(selectItem instanceof AllColumns){
            parserAllColumns((AllColumns) selectItem);
        }
    }

    private void parserAllColumns(AllColumns allColumns) {
        selectItemType = SelectItemType.COLUMN;
        expression = allColumns.toString();
    }

    private void parserAllTableColumns(AllTableColumns allTableColumns) {
        selectItemType = SelectItemType.COLUMN;
        expression = allTableColumns.toString();
    }

    /**
     * 表达式只有可能是列,常量,函数及子查询
     * @param selectExpressionItem
     */
    private void parserExpressionItem(SelectExpressionItem selectExpressionItem) {
        this.alias = selectExpressionItem.getAlias();
        Expression expression = selectExpressionItem.getExpression();
        if(expression instanceof Column){
            Column column = (Column) expression;
            this.expression = column.getFullyQualifiedName();
            this.selectItemType = SelectItemType.COLUMN;
        }else if(expression instanceof Function){
            Function function = (Function) expression;
            this.expression  = function.toString();
            this.selectItemType = SelectItemType.FUNCTION;
        }else if(expression instanceof NullValue || expression instanceof LongValue || expression instanceof StringValue || expression instanceof DoubleValue){
            this.expression = expression.toString();
            this.selectItemType = SelectItemType.CONSTANT;
        }else if(expression instanceof SubSelect){
            this.selectItemType = SelectItemType.SUB_QUERY;
            SubSelect subSelect = (SubSelect) expression;
            SelectBody selectBody = subSelect.getSelectBody();
            sqlSelectBody = new SqlSelectBody(selectBody);
            sqlSelectBody.parser();
        }
    }

    public SelectItemType getSelectItemType() {
        return selectItemType;
    }

    public Alias getAlias() {
        return alias;
    }

    public String getExpression() {
        return expression;
    }

    public SqlSelectBody getSqlSelectBody() {
        return sqlSelectBody;
    }
}
