package com.sanri.wechat.messages;

public class ImageMessage extends WechatMessage{
	//图片链接地址
	private String PicUrl;
	//消息ID号
	private long MsgId ;
	public String getPicUrl() {
		return PicUrl;
	}
	public void setPicUrl(String picUrl) {
		PicUrl = picUrl;
	}
	public long getMsgId() {
		return MsgId;
	}
	public void setMsgId(long msgId) {
		MsgId = msgId;
	}
	@Override
	public String toXML() {
		return null;
	}
}
