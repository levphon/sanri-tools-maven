package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.statement.select.SubJoin;

public class SqlSubJoin implements ParserItem {
    private SubJoin subJoin;

    public SqlSubJoin(SubJoin subJoin) {
        this.subJoin = subJoin;
    }

    public void parser() {

    }
}
