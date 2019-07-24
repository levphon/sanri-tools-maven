package com.sanri.wechat.messages;

import com.sanri.wechat.utils.MessageUtil;

/**
 * 文本消息
 * @author Majinglei
 *
 */
public class TextMessage extends WechatMessage{
	// 回复的消息内容  
	private String Content="";

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	@Override
	public String toXML() {
		return MessageUtil.textMessageToXml(this);
	}
}
