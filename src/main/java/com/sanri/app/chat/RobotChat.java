package com.sanri.app.chat;

import com.alibaba.fastjson.JSONObject;
import sanri.utils.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RobotChat {
    private String key = "a7c71cec1db2414ab1db17c6b61622fa";
    private String url = "http://www.tuling123.com/openapi/api";

    /**
     * 自动聊天
     * @param message
     * @return
     * @throws IOException
     */
    public String chat(String message) throws IOException {
        Map<String,String> params = new HashMap<String, String>();
        params.put("userid", "139794529");
        params.put("key", key);
        params.put("info", message);
        try {
            String post = HttpUtil.postFormData(url, params);
            JSONObject parseObject = JSONObject.parseObject(post);
            return parseObject.getString("text");
        } catch (IOException e) {
            throw e;
        }
    }
}
