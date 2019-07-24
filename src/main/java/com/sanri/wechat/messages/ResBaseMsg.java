package com.sanri.wechat.messages;

public abstract class ResBaseMsg {
	//普通用户 openid
	private String touser;
	//消息类型 text,news
	private String msgtype;
	public String getTouser() {
		return touser;
	}
	public void setTouser(String touser) {
		this.touser = touser;
	}
	public String getMsgtype() {
		return msgtype;
	}
	public void setMsgtype(String msgtype) {
		this.msgtype = msgtype;
	}
	
}
