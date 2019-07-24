package com.sanri.wechat.handler;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sanri.wechat.messages.WechatMessage;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-25下午6:56:55<br/>
 * 功能:消息处理器
 * 使用模板方法模式和命令模式,不支持回退
 *  <br/>
 */
public abstract class MessageHandler {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-8-25下午7:09:33<br/>
	 * 功能:子类实现获取 wechatMessage <br/>
	 * @return
	 */
	public abstract WechatMessage execute(Map<String, String> requestMap);
}
