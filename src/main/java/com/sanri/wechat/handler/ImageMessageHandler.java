package com.sanri.wechat.handler;

import java.util.Map;

import com.sanri.wechat.messages.ImageMessage;
import com.sanri.wechat.messages.WechatMessage;

/**
 * 
 * 创建时间:2017-8-26上午7:22:00<br/>
 * 创建者:sanri<br/>
 * 功能:图片消息处理器<br/>
 */
public class ImageMessageHandler extends MessageHandler {

	@Override
	public WechatMessage execute(Map<String, String> requestMap) {
		String picUrl = requestMap.get("PicUrl");
		String mediaId = requestMap.get("MediaId");
		
		return null;
	}

}
