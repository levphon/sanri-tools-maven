package com.sanri.app.jdbc.codegenerate;

import com.sanri.app.ConfigCenter;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 扩展 mybatis 类型映射
 */
public class RenamePolicyMybatisExtend implements  MybatisTypeMapper{
    private static final String configFile = "db_mapper_mybatis_type";
    private static final Map<String, Map<String, String>> mappers = new HashMap<String,  Map<String, String>>();
    private String dbType = "mysql";

    public RenamePolicyMybatisExtend() {
        //加载类型映射
        ConfigCenter configCenter = ConfigCenter.getInstance();
        String supports = configCenter.getString(configFile, "supports.dbType");
        String[] supportDbs = StringUtils.split(supports, ',');
        for (String supportDb : supportDbs) {
            Map<String, String> subConfigs = configCenter.getSubConfigs(configFile, supportDb);
            mappers.put(supportDb,subConfigs);
        }
    }

    @Override
    public String mapperJdbcTypeName(String columnType) {
        Map<String, String> subConfigs = mappers.get(dbType);
        return subConfigs.get(columnType);
    }

    /**
     * 注入数据库类型
     * @return
     */
    public String getDbType() {
        return dbType;
    }
}
