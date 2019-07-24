package com.sanri.app.jdbc.codegenerate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GenerateConfig {
	//实体生成配置
	private String framework;
	private String model;
	private String baseEntity;
	private String excludeColumns;
	private String interfaces;
	
	//包设置
	private String basePackage;			//基础包,用于引用工具类
	private String controllerPackage;
	private String entityPackage;
	private String servicePackage;
	private String serviceimplPackage;
	private String daoPackage;
	private String daoimplPackage;
	private Map<String,String> templates = new HashMap<String, String>();
	private List<String> tables = new  ArrayList<String>();

	//连接 信息
	private String connName;
	private String dbName;
	
	public String getFramework() {
		return framework;
	}
	public void setFramework(String framework) {
		this.framework = framework;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBaseEntity() {
		return baseEntity;
	}
	public void setBaseEntity(String baseEntity) {
		this.baseEntity = baseEntity;
	}
	public String getExcludeColumns() {
		return excludeColumns;
	}
	public void setExcludeColumns(String excludeColumns) {
		this.excludeColumns = excludeColumns;
	}
	public String getControllerPackage() {
		return controllerPackage;
	}
	public void setControllerPackage(String controllerPackage) {
		this.controllerPackage = controllerPackage;
	}
	public String getEntityPackage() {
		return entityPackage;
	}
	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}
	public String getServicePackage() {
		return servicePackage;
	}
	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}
	public String getServiceimplPackage() {
		return serviceimplPackage;
	}
	public void setServiceimplPackage(String serviceimplPackage) {
		this.serviceimplPackage = serviceimplPackage;
	}
	public String getDaoPackage() {
		return daoPackage;
	}
	public void setDaoPackage(String daoPackage) {
		this.daoPackage = daoPackage;
	}
	public String getDaoimplPackage() {
		return daoimplPackage;
	}
	public void setDaoimplPackage(String daoimplPackage) {
		this.daoimplPackage = daoimplPackage;
	}
	public Map<String, String> getTemplates() {
		return templates;
	}
	public void setTemplates(Map<String, String> templates) {
		this.templates = templates;
	}
	public List<String> getTables() {
		return tables;
	}
	public void setTables(List<String> tables) {
		this.tables = tables;
	}
	public String getConnName() {
		return connName;
	}
	public void setConnName(String connName) {
		this.connName = connName;
	}
	public String getDbName() {
		return dbName;
	}
	public void setDbName(String dbName) {
		this.dbName = dbName;
	}
	public String getBasePackage() {
		return basePackage;
	}
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}
	public String getInterfaces() {
		return interfaces;
	}
	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}
	
	
	
}
