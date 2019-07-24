<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="${daoPackage}.${entity}Mapper">
	<resultMap id="BaseResultMap" type="${entityPackage}.${entity}" >
		<id column="id" property="id" jdbcType="INTEGER" />
#foreach($resultMap in ${resultMaps})
			<result column="${resultMap.column}" property="${resultMap.property}" jdbcType="${resultMap.jdbcType}" />    
#end
	</resultMap>
	
	<sql id="Base_Column_List">
#foreach($col in ${columns})${col},#end
	</sql>
	
	<insert id="insert${entity}" parameterType="${entityPackage}.${entity}">
        <selectKey resultType="long" keyProperty="id" order="BEFORE">
            select ${tableName}_ID.Nextval as id from dual
        </selectKey>

		insert into
		${tableName}(<include refid="Base_Column_List" />)
		values(
#foreach($property in ${propertys})#{${property}},#end
		)
	</insert>
	
	<update id="update${entity}" parameterType="${voPackage}.${entity}">
		update ${tableName} set id = #{id}
		where id = #{id}
	</update>
	
	<delete id="delete${entity}" parameterType="string">
		delete from ${tableName}
		where id in
		<foreach collection="array" item="id" index="index" open="("
			close=")" separator=",">
			#{id}
		</foreach>
	</delete>
	
	<select id="listAll" resultMap="BaseResultMap">
		select <include refid="Base_Column_List" />
		from ${tableName}
	</select>
	
	<select id="find${entity}One" parameterType="Integer">
		select <include refid="Base_Column_List" />
		from ${tableName} where id = #{id}
	</select>
	
	<select id="find${entity}List" parameterType="map">
		select <include refid="Base_Column_List" />
		from ${tableName} t where 1=1
		ORDER BY t.CREATE_TIME DESC
	</select>
</mapper>