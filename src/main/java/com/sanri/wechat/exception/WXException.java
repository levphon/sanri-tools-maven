package com.sanri.wechat.exception;

/**
 * 作者:sanri <br/>
 * 时间:2017-8-22下午4:33:48<br/>
 * 功能:微信异常 <br/>
 */
public class WXException extends RuntimeException{
	private String errcode;
	private String errmsg;
	private String info;
	
	public WXException(String info,String errcode, String errmsg) {
		super(info+"|"+errcode+":"+errmsg);
		this.errcode = errcode;
		this.errmsg = errmsg;
	}
	
	public String getErrcode() {
		return errcode;
	}
	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
