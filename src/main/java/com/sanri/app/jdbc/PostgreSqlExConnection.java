package com.sanri.app.jdbc;

import com.sanri.app.jdbc.codegenerate.RenamePolicyMybatisExtend;
import com.sanri.app.postman.JdbcConnDetail;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.lang.ObjectUtils;
import org.postgresql.ds.PGSimpleDataSource;
import sanri.utils.PropertyEditUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgreSqlExConnection extends ExConnection {
    public static final String dbType = "postgresql";
    public PostgreSqlExConnection(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    @Override
    protected DataSource copyDataSource(String schemaName) {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        PropertyEditUtil.copyExclude(pgSimpleDataSource,dataSource);
        pgSimpleDataSource.setDatabaseName(schemaName);
        return pgSimpleDataSource;
    }

    @Override
    protected List<Schema> refreshSchemas() throws SQLException {
        List<Schema> schemas = new ArrayList<Schema>();
        List<String> databases = mainQueryRunner.query("SELECT datname FROM pg_database", new TypeListHandler<String>());
        for (String database : databases) {
            schemas.add(new Schema(database));
        }
        return schemas;
    }

    @Override
    protected List<Table> refreshTables(String schemaName) throws SQLException {
//        String sql = "select relname as name,cast(obj_description(relfilenode,'pg_class') as varchar) as comment from pg_class c where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname";
        String sql = "select concat(n.nspname,'.',relname) as name,cast(obj_description(relfilenode,'pg_class') as varchar) as comment from pg_class c\n" +
                "inner join pg_namespace n on n.oid = c.relnamespace\n" +
                "where relkind = 'r' and relname not like 'pg_%' and relname not like 'sql_%' order by relname ";
        Schema schema = schemas.get(schemaName);
        QueryRunner queryRunner = new QueryRunner(schema.dataSource());
        List<Table> tables = queryRunner.query(sql, new ResultSetHandler<List<Table>>() {
            @Override
            public List<Table> handle(ResultSet resultSet) throws SQLException {
                List<Table> tables = new ArrayList<Table>();
                while (resultSet.next()) {
                    String tableName = ObjectUtils.toString(resultSet.getString("name")).toLowerCase();
                    String comments = resultSet.getString("comment");
                    tables.add(new Table(tableName, comments));
                }
                return tables;
            }
        });
        return tables;
    }

    @Override
    protected List<Column> refreshColumns(String schemaName, String tableName) throws SQLException {
        Schema schema = schemas.get(schemaName);
        QueryRunner queryRunner = new QueryRunner(schema.dataSource());
        tableName = tableName.split("\\.")[1];
        String sql = "SELECT col_description(a.attrelid,a.attnum) as comment,format_type(a.atttypid,a.atttypmod) as type,a.attname as name, a.attnotnull as notnull FROM pg_class as c,pg_attribute as a where c.relname = '"+tableName+"' and a.attrelid = c.oid and a.attnum>0";
        List<Column> columns = queryRunner.query(sql, new ResultSetHandler<List<Column>>() {
            @Override
            public List<Column> handle(ResultSet resultSet) throws SQLException {
                List<Column> columns = new ArrayList<Column>();
                while (resultSet.next()){
                    String columnName = resultSet.getString(3);
                    String dataType = resultSet.getString(2);
                    String comment = resultSet.getString(1);

                    ColumnType columnType = new ColumnType(dataType);
                    Column column = new Column(columnName, columnType, comment);
                    columns.add(column);
                }
                return columns;
            }
        });
        return columns;
    }

    @Override
    public String getDatabase() {
        return ((PGSimpleDataSource)dataSource).getDatabaseName();
    }

    @Override
    public JdbcConnDetail getConnDetail() {
        PGSimpleDataSource pgSimpleDataSource = (PGSimpleDataSource) dataSource;
        JdbcConnDetail jdbcConnDetail = new JdbcConnDetail();
        jdbcConnDetail.config(dbType,pgSimpleDataSource.getServerName(),pgSimpleDataSource.getPortNumber(),pgSimpleDataSource.getUser(),pgSimpleDataSource.getDatabaseName());
        String password = pgSimpleDataSource.getPassword();
        jdbcConnDetail.setUserpass(password);
        return jdbcConnDetail;
    }

    @Override
    public String ddL(String schemaName, String tableName) {
        return "pg 无法实现,使用 pg_dump 来查询";
    }

    static RenamePolicyMybatisExtend renamePolicyMybatisExtend = new RenamePolicyMybatisExtend(dbType);
    @Override
    public RenamePolicyMybatisExtend getRenamePolicyMybatis() {
        return renamePolicyMybatisExtend;
    }
}
