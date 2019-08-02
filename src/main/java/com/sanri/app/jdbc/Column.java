package com.sanri.app.jdbc;

public class Column {
    private String columnName;
    private String comments;
    private ColumnType columnType;
    private boolean primaryKey;

    public Column(String columnName,ColumnType columnType, String comments) {
        this.columnType = columnType;
        this.columnName = columnName;
        this.comments = comments;
    }

    public Column(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getComments() {
        return comments;
    }

    public ColumnType getColumnType() {
        return columnType;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
}
