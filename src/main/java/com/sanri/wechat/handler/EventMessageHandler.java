package com.sanri.wechat.handler;

import java.util.Map;

import com.sanri.wechat.messages.TextMessage;
import com.sanri.wechat.messages.WechatMessage;
import com.sanri.wechat.utils.MessageUtil;

/**
 * 
 * 创建时间:2017-8-25下午10:55:17<br/>
 * 创建者:sanri<br/>
 * 功能:事件消息处理<br/>
 * 微信事件包含 
 */
public class EventMessageHandler extends MessageHandler{

	@Override
	public WechatMessage execute(Map<String, String> requestMap) {
		String event = requestMap.get("Event");
		if(MessageUtil.EVENT_TYPE_CLICK.equals(event)){
			//菜单点击事件
			TextMessage textMessage = new TextMessage();
			textMessage.setContent("功能未实现 ");
			return textMessage;
		}else{
			TextMessage textMessage = new TextMessage();
			if(MessageUtil.EVENT_TYPE_SUBSCRIBE.equals(event)){
				textMessage.setContent("欢迎使用三日公众号");
			}else if(MessageUtil.EVENT_TYPE_UNSUBSCRIBE.equals(event)){
				textMessage.setContent("你已取消订阅三日公众号");
			}
			return textMessage;
		}
	}

}
