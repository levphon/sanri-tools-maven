package com.sanri.app.postman;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-7下午1:03:29<br/>
 * 功能:提供给请求服务的来回消息实体 <br/>
 */
public class MsgEntity {
	//支持三种 application/json;application/x-www-form-urlencoded;text/xml
	private String contentType = "application/json";
	private String body;
	private int statusCode;
	
	public MsgEntity(){}

	public MsgEntity(String contentType) {
		super();
		this.contentType = contentType;
	}
	
	public MsgEntity(String contentType, String body) {
		super();
		this.contentType = contentType;
		this.body = body;
	}

	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
}
