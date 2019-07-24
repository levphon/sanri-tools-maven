package com.sanri.app.sqlparser.items;

import com.sanri.app.sqlparser.ParserItem;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.schema.Table;

public class SqlTable implements ParserItem {
    private Table table;

    private Alias alias;
    private String tableName;

    public SqlTable(Table table) {
        this.table = table;
    }

    public void parser() {
        this.alias = table.getAlias();
        this.tableName = table.getFullyQualifiedName();
    }

    public Alias getAlias() {
        return alias;
    }

    public String getTableName() {
        return tableName;
    }
}
