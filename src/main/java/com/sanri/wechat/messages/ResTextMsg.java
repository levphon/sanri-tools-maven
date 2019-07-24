package com.sanri.wechat.messages;

import java.io.Serializable;

public class ResTextMsg extends ResBaseMsg implements Serializable{
	private static final long serialVersionUID = -3656439331238940484L;
	//文本消息内容标签
	private Text text;
	
	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}
	//文本消息内容
	public static class Text implements Serializable{
		
		private static final long serialVersionUID = -6516977723357810139L;

		public Text(String content) {
			this.content = content;
		}

		private final String content;

		public String getContent() {
			return content;
		}
		
	}
	
//	public static void main(String[] args) {
//		//otpkcuHmK6nhZ3Py6KI1BYfWHVw8
//		ResTextMsg textMsg = new ResTextMsg();
//		textMsg.setTouser("otpkcuHmK6nhZ3Py6KI1BYfWHVw8");
//		textMsg.setMsgtype("text");
//		textMsg.setText(new ResTextMsg.Text("天道不酬勤，不是吗？"));
//		String jsonStr = JSONObject.fromObject(textMsg).toString();
//		String httpsUrl = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
//		httpsUrl = httpsUrl.replace("ACCESS_TOKEN", WXAccessToken.INSTANCE.getAccessToken());
//		JSONObject json = WXUtil.httpsRequest(httpsUrl, "POST", jsonStr);
//		System.out.println(json.toString());
//	}
}

