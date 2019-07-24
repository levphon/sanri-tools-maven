package com.sanri.wechat.handler;

import java.util.HashMap;
import java.util.Map;

import sanri.utils.HttpUtil;

import com.alibaba.fastjson.JSONObject;
import com.sanri.wechat.messages.TextMessage;
import com.sanri.wechat.messages.WechatMessage;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-25下午7:10:26<br/>
 * 功能:文本消息处理器 <br/>
 * 此处使用图灵消息提供
 */
public class TextMessageHandler extends MessageHandler {
	private final static String key = "a7c71cec1db2414ab1db17c6b61622fa";
	private final static String url = "http://www.tuling123.com/openapi/api";
	@Override
	public WechatMessage execute(Map<String, String> requestMap) {
		String content = requestMap.get("Content").trim();
		Map<String,String> params = new HashMap<String, String>();
		//userid 使用公众号 
		params.put("userid", "gh_31cdd91ccb2d");
		params.put("key", key);
		params.put("info", content);
		try {
			String jsonString = HttpUtil.postFormData(url, params);
			JSONObject parseObject = JSONObject.parseObject(jsonString);
			int code = parseObject.getIntValue("code");
			switch (code) {
			case 100000:
				//文本类消息返回
				TextMessage textMessage = new TextMessage();
				textMessage.setContent(parseObject.getString("text"));
				return textMessage;
			case 200000:
				//链接类返回
				TextMessage linkMessage = new TextMessage();
				linkMessage.setContent(parseObject.getString("url"));
				return linkMessage;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//其它情况返回文本消息,提示用户输入的内容
		TextMessage textMessage = new TextMessage();
		textMessage.setContent("您的输入为:"+content+",目前公众号无法解析您的请求,请稍后再试");
		return textMessage;
	}

}
