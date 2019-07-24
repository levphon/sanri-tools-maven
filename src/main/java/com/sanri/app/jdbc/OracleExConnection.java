package com.sanri.app.jdbc;

import com.sanri.app.postman.JdbcConnDetail;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

public class OracleExConnection extends ExConnection {
    public static final String dbType = "oracle";

    public OracleExConnection(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    @Override
    protected DataSource copyDataSource(String schemaName) {
        return null;
    }

    @Override
    protected List<Schema> refreshSchemas() throws SQLException {
        return null;
    }

    @Override
    protected List<Table> refreshTables(String schemaName) throws SQLException {
        return null;
    }

    @Override
    protected List<Column> refreshColumns(String schemaName, String tableName) throws SQLException {
        return null;
    }

    @Override
    public String getDatabase() {
        return null;
    }

    @Override
    public JdbcConnDetail getConnDetail() {
        return null;
    }

    @Override
    public String ddL(String schemaName, String tableName) {
        return null;
    }
}
