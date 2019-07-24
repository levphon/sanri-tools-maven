package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;

public class SqlSelectStatement implements ParserItem {
    private Select select;
    private SqlSelectBody sqlSelectBody;

    public SqlSelectStatement(Select select) {
        this.select = select;
    }

    public void parser() {
        SelectBody selectBody = select.getSelectBody();
        sqlSelectBody = new SqlSelectBody(selectBody);
        sqlSelectBody.parser();
    }

    public SqlSelectBody getSqlSelectBody() {
        return sqlSelectBody;
    }
}
