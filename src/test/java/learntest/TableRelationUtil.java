package com.linger.demo.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubSelect;

public class TableRelationUtil {
    static Map<String, String> tableMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        // String sql = "select t1.a,t2.b from table1 t1 left join table2 t2 on t1.a = t2.b";
        //String sql = "select *from (select t1.a,t2.b from table1 t1 left join table2 t2 on t1.a = t2.b) a";

        //String sql = "select t1.a,t2.b from table1 t1 left join table2 t2 on t1.a = t2.b left join table3 t3 on t2.c=t3.d where t1.a = ?";
        //String sql = "select t1.a,t2.b from table1 as t1,table2 as t2,table3 t3 where t1.a = t2.b and t2.c=t3.d and t1.a = ?";

        String sql = "select t1 from table1 t1 union all select t2.b,t3.c from table2 t2,table3 t3 where t2.b = t3.c";
        sql = "SELECT c.* from trade_ix.t_item_account_group_symbol_cata  a\n" +
                "                    inner join t_account_group  b on a.account_groupid = b.trade_group_id\n" +
                "                    inner join trade_ix.t_item_symbol c on c.id = a.symbolid\n" +
                "                    inner join t_account_info d on d.account_group_id = b.id\n" +
                "where b.deleted = false and symbolid != 0 and d.gts2_customer_id = 1";
        getTableRelation(sql);
    }

    public static void getTableRelation(String sql) throws Exception {
        Statement stmt = CCJSqlParserUtil.parse(sql);
        if (stmt instanceof Select) {
            Select select = (Select) stmt;
            SelectBody selectBody = select.getSelectBody();
            alise(selectBody);
        }
    }

    private static void alise(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            PlainSelect plainSelect = (PlainSelect) selectBody;
            FromItem fromItem = plainSelect.getFromItem();
            List<Join> joins = plainSelect.getJoins();

            if(joins != null) {
                for (Join join : joins) {
                    Expression onExpression = join.getOnExpression();
                    FromItem rightItem = join.getRightItem();
                    if (rightItem instanceof Table) {
                        Table table = (Table) rightItem;
                        tableMap.put(table.getAlias().getName(), table.getName());
                    }
                    doExpression(onExpression);
                }
            }

            if (fromItem instanceof Table) {
                Table table = (Table) fromItem;
                tableMap.put(table.getAlias().getName(), table.getName());
            }else if(fromItem instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) fromItem;
                SelectBody subSelectBody = subSelect.getSelectBody();
                alise(subSelectBody);
            }

            doExpression(plainSelect.getWhere());
        }else if(selectBody instanceof SetOperationList) {
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selects = setOperationList.getSelects();
            for (SelectBody selectBody3 : selects) {
                alise(selectBody3);
            }
        }
    }

    private static void doExpression(Expression expression) {
        if (expression instanceof EqualsTo) {
            EqualsTo equalsTo = (EqualsTo) expression;
            Expression rightExpression = equalsTo.getRightExpression();
            Expression leftExpression = equalsTo.getLeftExpression();
            if (rightExpression instanceof Column && leftExpression instanceof Column) {
                Column rightColumn = (Column) rightExpression;
                Column leftColumn = (Column) leftExpression;
                System.out.println(tableMap.get(rightColumn.getTable().toString()) + "表的" + rightColumn.getColumnName() + "字段 -> "
                        + tableMap.get(leftColumn.getTable().toString()) + "表的" + leftColumn.getColumnName() + "字段");
            }
        }else if(expression instanceof AndExpression){
            AndExpression andExpression = (AndExpression) expression;
            Expression leftExpression = andExpression.getLeftExpression();
            doExpression(leftExpression);
            Expression rightExpression = andExpression.getRightExpression();
            doExpression(rightExpression);
        }
    }

}
