package com.sanri.app.jdbc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Table {
    private String tableName;
    private String comments;
    private Map<String,Column> columns = new HashMap<String, Column>();

    public Table(String tableName) {
        this.tableName = tableName;
    }

    public Table(String tableName, String comments) {
        this.tableName = tableName;
        this.comments = comments;
    }

    public String getTableName() {
        return tableName;
    }

    public String getComments() {
        return comments;
    }

    public void addColumn(Column column){
        columns.put(column.getColumnName(),column);
    }
    public void clearColums(){
        this.columns.clear();
    }

    public Column getColumn(String columnName){
        return columns.get(columnName);
    }

    public boolean isEmptyColumns(){
        return columns.isEmpty();
    }

    public List<Column> getColumns() {
        return new ArrayList<Column>(columns.values());
    }
}
