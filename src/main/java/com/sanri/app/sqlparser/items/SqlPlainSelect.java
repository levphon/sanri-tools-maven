package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectItem;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class SqlPlainSelect implements ParserItem {
    private PlainSelect plainSelect;

    private List<SqlSelectItem> sqlSelectItems = new ArrayList<>();
    private SqlFromItem sqlFromItem ;
    private SqlWhere sqlWhere;
    private List<SqlJoin> sqlJoins = new ArrayList<>();

    public SqlPlainSelect(PlainSelect plainSelect) {
        this.plainSelect = plainSelect;
    }

    public void parser() {
        // select 元素解析
        List<SelectItem> selectItems = plainSelect.getSelectItems();
        for (SelectItem selectItem : selectItems) {
            SqlSelectItem sqlSelectItem = new SqlSelectItem(selectItem);
            this.sqlSelectItems.add(sqlSelectItem);
            sqlSelectItem.parser();
        }

        // from 信息解析
        FromItem fromItem = plainSelect.getFromItem();
        this.sqlFromItem = new SqlFromItem(fromItem);
        sqlFromItem.parser();

        // 解析表达式解析
        Expression where = plainSelect.getWhere();
        if(where != null) {
            this.sqlWhere = new SqlWhere(where);
            sqlWhere.parser();
        }

        List<Join> joins = plainSelect.getJoins();
        if(CollectionUtils.isNotEmpty(joins)) {
            for (Join join : joins) {
                SqlJoin sqlJoin = new SqlJoin(join);
                this.sqlJoins.add(sqlJoin);
                sqlJoin.parser();
            }
        }
    }

    public List<SqlSelectItem> getSqlSelectItems() {
        return sqlSelectItems;
    }

    public SqlFromItem getSqlFromItem() {
        return sqlFromItem;
    }

    public SqlWhere getSqlWhere() {
        return sqlWhere;
    }

    public List<SqlJoin> getSqlJoins() {
        return sqlJoins;
    }
}
