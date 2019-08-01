<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="$!{namespace}">
    <!-- base result map -->
    <resultMap id="BaseResultMap" type="$!{beanType}">
        <id column="id" jdbcType="INTEGER" property="id"/>
    #foreach( $column in $table.columns)
    #if ($column.columnName != 'id')
    <result column="$column.columnName" jdbcType="$typeMapper.mapperJdbcTypeName($column.columnType.dataType)" property="$renamePolicy.mapperPropertyName($column.columnName)"/>
    #end
    #end
    </resultMap>

    <!-- base column list -->
    <sql>#foreach( $column in $table.columns) #if ($column.columnName == 'id') id #else  ,$column.columnName #end #end </sql>
</mapper>