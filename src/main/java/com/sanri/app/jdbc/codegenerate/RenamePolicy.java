package com.sanri.app.jdbc.codegenerate;

public interface RenamePolicy {
	/**
	 * 
	 * 功能:从表名映射到类名<br/>
	 * 创建时间:2016-9-25下午3:15:09<br/>
	 * 作者：sanri<br/>
	 */
	String mapperClassName(String tableName);
	/**
	 * 
	 * 功能:从列名映射到字段名<br/>
	 * 创建时间:2016-9-25下午3:15:09<br/>
	 * 作者：sanri<br/>
	 */
	String mapperPropertyName(String columnName);
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-4-24下午1:56:27<br/>
	 * 功能:从数据库字段类型到 java 类型的映射  <br/>
	 * 入参: <br/>
	 */
	String mapperPropertyType(String columnType);
}