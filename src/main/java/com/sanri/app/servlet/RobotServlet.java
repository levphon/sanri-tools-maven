package com.sanri.app.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import sanri.utils.HttpUtil;

import com.alibaba.fastjson.JSONObject;
import com.sanri.app.BaseServlet;
import com.sanri.frame.RequestMapping;

@RequestMapping("/robot")
public class RobotServlet extends BaseServlet {
	private String key = "a7c71cec1db2414ab1db17c6b61622fa";
	private String url = "http://www.tuling123.com/openapi/api";
	
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-4-24上午11:08:30<br/>
	 * 功能:调用图灵 api 进行聊天 <br/>
	 * 入参: <br/>
	 */
	public String chat(String info,HttpSession session){
		String userId = session.getId();
		Map<String,String> params = new HashMap<String, String>();
		params.put("userid", "139794529");
		params.put("key", key);
		params.put("info", info);
		try {
			String post = HttpUtil.postFormData(url, params);
			JSONObject parseObject = JSONObject.parseObject(post);
			logger.info(parseObject);
			return parseObject.getString("text");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
