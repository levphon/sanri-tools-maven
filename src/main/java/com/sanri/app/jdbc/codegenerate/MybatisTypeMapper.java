package com.sanri.app.jdbc.codegenerate;

public interface MybatisTypeMapper {

    /**
     * 由数据库类型映射到mybatis jdbc 类型
     * @param javaType
     * @return
     */
    String mapperJdbcTypeName(String columnType);
}
