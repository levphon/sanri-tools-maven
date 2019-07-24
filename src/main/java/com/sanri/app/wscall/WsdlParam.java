package com.sanri.app.wscall;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-6-21下午1:27:31<br/>
 * 功能: webservice 参数 <br/>
 */
public class WsdlParam {
	//最小,最大出现次数,没有说明或为 unbounded 时设置为 -1
	private long minOccurs;
	private long maxOccurs;
	//参数默认值
	private String defaultVal;
	//参数固定值
	private String fixed;
	
	private String paramName;
	
	private WsdlType paramType;
	private boolean array;

	public String getDefaultVal() {
		return defaultVal;
	}

	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}

	public String getFixed() {
		return fixed;
	}

	public void setFixed(String fixed) {
		this.fixed = fixed;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public WsdlType getParamType() {
		return paramType;
	}

	public void setParamType(WsdlType paramType) {
		this.paramType = paramType;
	}

	public boolean isArray() {
		return array;
	}

	public void setArray(boolean array) {
		this.array = array;
	}

	public long getMinOccurs() {
		return minOccurs;
	}

	public void setMinOccurs(long minOccurs) {
		this.minOccurs = minOccurs;
	}

	public long getMaxOccurs() {
		return maxOccurs;
	}

	public void setMaxOccurs(long maxOccurs) {
		this.maxOccurs = maxOccurs;
	}
	@Override
	public String toString() {
//		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
		return JSONObject.toJSONString(this);
	}
}
