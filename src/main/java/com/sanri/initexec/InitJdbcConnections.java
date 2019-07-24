package com.sanri.initexec;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.sanri.app.ConfigCenter;
import com.sanri.app.jdbc.ExConnection;
import com.sanri.app.jdbc.MysqlExConnection;
import com.sanri.app.jdbc.PostgreSqlExConnection;
import com.sanri.app.postman.JdbcConnDetail;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.postgresql.ds.PGSimpleDataSource;
import sanri.utils.PropertyEditUtil;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class InitJdbcConnections {
    private Log logger = LogFactory.getLog(getClass());
    /** 保存所有的连接信息 */
    public static Map<String,ExConnection> CONNECTIONS = new HashMap<String, ExConnection>();

    /**
     * 保存一个连接
     * @param connectionInfo
     */
    public static void saveConnection(JdbcConnDetail connectionInfo) throws SQLException {
        if("mysql".equalsIgnoreCase(connectionInfo.getDbType())){
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setServerName(connectionInfo.getHost());
            dataSource.setPort(Integer.parseInt(connectionInfo.getPort()));
            dataSource.setDatabaseName(connectionInfo.getDatabase());
            dataSource.setUser(connectionInfo.getUsername());
            dataSource.setPassword(connectionInfo.getUserpass());
            ExConnection exConnection = ExConnection.newInstance("mysql", connectionInfo.getDatabase(), dataSource);
            CONNECTIONS.put(connectionInfo.getName(),exConnection);
        }
    }

    @PostConstruct
    public void execute(){
        //加载默认连接信息,并初始化库
        logger.info("加载默认连接信息,并初始化库");

        ConfigCenter configCenter = ConfigCenter.getInstance();
        String openDbs = configCenter.getString("jdbcdefault", "open.dbs");
        String[] conns = StringUtils.split(openDbs,',');
        if(ArrayUtils.isNotEmpty(conns)){
            for (String conn : conns) {
                Map<String, String> dbConfig = configCenter.getSubConfigs("jdbcdefault", conn);
                String dbType = dbConfig.get("dbType");

                //动态创建数据源
                DataSource dataSource = dynamicDatasource(dbConfig);
                //创建扩展连接并保存
                try {
                    ExConnection exConnection = ExConnection.newInstance(dbType,conn,dataSource);
                    CONNECTIONS.put(conn,exConnection);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 根据数据类型创建数据源
     * @param dbConfig
     * @return
     */
    private static DataSource dynamicDatasource(Map<String, String> dbConfig) {
        DataSource dataSource = null;

        String dbType = dbConfig.get("dbType");
        if(MysqlExConnection.dbType.equalsIgnoreCase(dbType)){
            dataSource = new MysqlDataSource();
            PropertyEditUtil.populateMapData(dataSource,dbConfig,"dbType");
            String url = ((MysqlDataSource) dataSource).getUrl();

            //mysql 数据库需要单独添加部分参数
            url += "?allowMultiQueries=true&characterEncoding=utf-8&useUnicode=true";
            ((MysqlDataSource) dataSource).setUrl(url);
        }else if(PostgreSqlExConnection.dbType.equalsIgnoreCase(dbType)){
            dataSource = new PGSimpleDataSource();
            PropertyEditUtil.populateMapData(dataSource,dbConfig,"dbType");
        }
        return dataSource;
    }
}
