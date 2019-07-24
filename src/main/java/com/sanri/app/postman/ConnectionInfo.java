package com.sanri.app.postman;

import sanri.utils.PropertyEditUtil;

import javax.sql.DataSource;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-7-6下午2:02:25<br/>
 * 功能:连接信息 <br/>
 */
public class ConnectionInfo {
	private String name;
	private String host;
	private String port;
	private String username;
	private String userpass;
	private String dbType;
	private String database;				//这里对于 mysql 连接数据库,对于 oracle 为实例名
	private String spellingRule="lower";

	public ConnectionInfo() {
	}

	public ConnectionInfo(String name, String host, String port, String username, String userpass, String database) {
		this.name = name;
		this.host = host;
		this.port = port;
		this.username = username;
		this.userpass = userpass;
		this.database = database;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getUserpass() {
		return userpass;
	}
	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}
	public String getDbType() {
		return dbType;
	}
	public void setDbType(String dbType) {
		this.dbType = dbType;
	}
	public String getSpellingRule() {
		return spellingRule;
	}
	public void setSpellingRule(String spellingRule) {
		this.spellingRule = spellingRule;
	}
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}

	/**
	 * 转换成连接串
	 * @return
	 */
    public String toConnString() {
		String url  = "";
		if("mysql".equalsIgnoreCase(this.dbType)){
			url= "jdbc:mysql://"+host+":"+port+"/"+database+"?characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNulll&allowMultiQueries=true";
		}else if("oracle".equalsIgnoreCase(dbType)){
			url = "jdbc:oracle:thin:@"+host+":"+port+database;
		}
		return url;
    }

	/**
	 * 复制并修改数据库
	 * @param database
	 * @return
	 */
	public ConnectionInfo copyAndUpdateSchema(String database){
    	ConnectionInfo connectionInfo = new ConnectionInfo();
		PropertyEditUtil.copyExclude(connectionInfo,this);
		connectionInfo.setDatabase(database);
    	return connectionInfo;
	}
}
