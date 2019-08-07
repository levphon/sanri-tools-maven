package com.sanri.app.jdbc;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sanri.app.jdbc.codegenerate.RenamePolicyMybatisExtend;
import com.sanri.app.postman.JdbcConnDetail;
import oracle.jdbc.pool.OracleDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.commons.lang.ObjectUtils;
import sanri.utils.PropertyEditUtil;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OracleExConnection extends ExConnection {
    public static final String dbType = "oracle";

    public OracleExConnection(DataSource dataSource) throws SQLException {
        super(dataSource);
    }

    @Override
    protected DataSource copyDataSource(String schemaName) throws SQLException {
        OracleDataSource oracleDataSource = new OracleDataSource();
        PropertyEditUtil.copyExclude(oracleDataSource,dataSource);
        oracleDataSource.setDatabaseName(schemaName);
        return oracleDataSource;
    }

    @Override
    protected List<Schema> refreshSchemas() throws SQLException {
        TypeListHandler<String> resultSetHandler = new TypeListHandler<String>();
        List<String> databases = mainQueryRunner.query("select * from v$database",resultSetHandler );
        List<Schema> schemas = new ArrayList<Schema>();
        for (String database : databases) {
            schemas.add(new Schema(database));
        }

        return schemas;
    }

    @Override
    protected List<Table> refreshTables(String schemaName) throws SQLException {
        Schema schema = schemas.get(schemaName);
        OracleDataSource dataSource = (OracleDataSource) schema.dataSource();
        QueryRunner queryRunner = new QueryRunner(dataSource);
//        //查询当前数据库所有表的主键信息
//        String primaryKeySql ="SELECT TABLE_NAME as tableName,COLUMN_NAME as primaryKey FROM INFORMATION_SCHEMA.`KEY_COLUMN_USAGE` WHERE  constraint_name='PRIMARY' and CONSTRAINT_SCHEMA = '"+schemaName+"'";
//        Map<String, Set<String>> tablePrimaryMap = queryPrimaryKeys(queryRunner, primaryKeySql);

        List<Table> tables = queryRunner.query("select dt.table_name name,dtc.comments comment " +
                "from user_tables dt,user_tab_comments dtc,user_objects uo " +
                "where dt.table_name = dtc.table_name and dt.table_name = uo.object_name and uo.object_type='TABLE' and owner = '"+dataSource.getUser()+"' ", new ResultSetHandler<List<Table>>() {
            @Override
            public List<Table> handle(ResultSet resultSet) throws SQLException {
                List<Table> tables = new ArrayList<Table>();
                while (resultSet.next()) {
                    String tableName = ObjectUtils.toString(resultSet.getString("name")).toLowerCase();
                    String comments = resultSet.getString("comment");

                    Table table = new Table(tableName, comments);
//                    Set<String> primaryKeys = tablePrimaryMap.get(tableName);
//                    if(primaryKeys != null) {
//                        table.setPrimaryKeys(primaryKeys);
//                    }
                    tables.add(table);
                }
                return tables;
            }
        });
        return tables;
    }

    @Override
    protected List<Column> refreshColumns(String schemaName, String tableName) throws SQLException {
        return null;
    }

    @Override
    public String getDatabase() {
        return ((OracleDataSource)dataSource).getDatabaseName();
    }

    @Override
    public JdbcConnDetail getConnDetail() {
        OracleDataSource dataSource = (OracleDataSource) getDataSource();
        JdbcConnDetail jdbcConnDetail = new JdbcConnDetail();
        jdbcConnDetail.config(dbType,dataSource.getServerName(),dataSource.getPortNumber(),dataSource.getUser(),dataSource.getDatabaseName());
//        jdbcConnDetail.setUserpass(getPassword(dataSource));
        return jdbcConnDetail;
    }

    @Override
    public String ddL(String schemaName, String tableName) throws SQLException {
        String sql = "select dbms_metadata.get_ddl('"+tableName+"','COMMUNITY') from dual";
        Schema schema = schemas.get(schemaName);
        QueryRunner queryRunner = new QueryRunner(schema.dataSource());
        return queryRunner.query(sql, new ScalarHandler<String>(1));
    }

    static RenamePolicyMybatisExtend renamePolicyMybatisExtend = new RenamePolicyMybatisExtend(dbType);
    @Override
    public RenamePolicyMybatisExtend getRenamePolicyMybatis() {
        return renamePolicyMybatisExtend;
    }

    @Override
    public String getDbType() {
        return dbType;
    }
}
