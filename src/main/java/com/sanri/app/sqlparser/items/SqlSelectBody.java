package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.*;

import java.util.ArrayList;
import java.util.List;

public class SqlSelectBody implements ParserItem {
    private SelectBody selectBody;
    private List<SqlPlainSelect> sqlPlainSelects = new ArrayList<>();

    public SqlSelectBody(SelectBody selectBody) {
        this.selectBody = selectBody;
    }

    public void parser() {
        if(selectBody instanceof PlainSelect){
            SqlPlainSelect sqlPlainSelect = new SqlPlainSelect((PlainSelect) selectBody);
            sqlPlainSelects.add(sqlPlainSelect);
            sqlPlainSelect.parser();
        }else if(selectBody instanceof SetOperationList){
            SetOperationList setOperationList = (SetOperationList) selectBody;
            List<SelectBody> selects = setOperationList.getSelects();
            for (SelectBody selectBody : selects) {
                SqlSelectBody sqlSelectBody = new SqlSelectBody(selectBody);
                sqlSelectBody.parser();
                sqlPlainSelects.addAll(sqlSelectBody.sqlPlainSelects);
            }
        }
    }

    public List<SqlPlainSelect> getSqlPlainSelects() {
        return sqlPlainSelects;
    }
}
