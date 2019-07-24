package com.sanri.wechat.messages;

import java.io.Writer;

import org.apache.commons.beanutils.BeanUtils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

/***
 * 响应微信信息基类
 * 
 * @author Majinglei
 */
public abstract class WechatMessage {
	// 接收方帐号（收到的OpenID）
	protected String ToUserName;
	// 开发者微信号
	protected String FromUserName;
	// 消息创建时间 （整型）
	protected long CreateTime;
	// 消息类型（text/music/news/image）
	protected String MsgType;
	// 位0x0001被标志时，星标刚收到的消息
	protected int FuncFlag;
	
	public abstract String toXML();
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-8-25下午5:55:34<br/>
	 * 功能:注入默认数据 <br/>
	 * @param ToUserName
	 * @param FromUserName
	 */
	public void populate(String ToUserName,String FromUserName){
		this.FuncFlag = 0;
		this.CreateTime = System.currentTimeMillis();
		this.FromUserName = FromUserName;
		this.ToUserName = ToUserName;
	}
	
	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public int getFuncFlag() {
		return FuncFlag;
	}

	public void setFuncFlag(int funcFlag) {
		FuncFlag = funcFlag;
	}
}
