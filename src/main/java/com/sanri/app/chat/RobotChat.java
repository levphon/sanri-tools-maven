package com.sanri.app.chat;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.entity.ContentType;
import sanri.utils.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RobotChat {
    private String key = "a7c71cec1db2414ab1db17c6b61622fa";
//    private String url = "http://www.tuling123.com/openapi/api";
    private String url = "http://openapi.tuling123.com/openapi/api/v2";


//    public String chat(String message){
//        Map<String,String> params = new HashMap<String, String>();
//        params.put("userid", "139794529");
//        params.put("key", key);
//        params.put("info", message);
//        try {
//            String post = HttpUtil.postFormData(url, params);
//            JSONObject parseObject = JSONObject.parseObject(post);
//            return parseObject.getString("text");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
    String userId = "139794529";
    public String chat(String message){
        Request textRequest = createTextRequest(message, key, userId);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("v",textRequest);
        JSONObject request = jsonObject.getJSONObject("v");
        try {
            JSONObject response = HttpUtil.postJSON(url,request,HttpUtil.JSON_UTF8);
            JSONArray results = response.getJSONArray("results");
            String responseText = results.getJSONObject(0).getJSONObject("values").getString("text");
            return responseText;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Request createTextRequest(String info,String apiKey,String userId){
        UserInfo userInfo = new UserInfo(apiKey, userId);
        Perception perception = new Perception(info);
        Request request = new Request(0, perception, userInfo);
        return request;
    }

    class Request{
        private int reqType;
        private Perception perception;
        private UserInfo userInfo;

        public Request() {
        }

        public Request(int reqType, Perception perception,UserInfo userInfo) {
            this.reqType = reqType;
            this.perception = perception;
            this.userInfo = userInfo;
        }

        public int getReqType() {
            return reqType;
        }

        public void setReqType(int reqType) {
            this.reqType = reqType;
        }

        public Perception getPerception() {
            return perception;
        }

        public void setPerception(Perception perception) {
            this.perception = perception;
        }

        public UserInfo getUserInfo() {
            return userInfo;
        }

        public void setUserInfo(UserInfo userInfo) {
            this.userInfo = userInfo;
        }
    }
    class UserInfo{
        private String apiKey;
        private String userId;

        public UserInfo(String apiKey, String userId) {
            this.apiKey = apiKey;
            this.userId = userId;
        }

        public String getApiKey() {
            return apiKey;
        }

        public void setApiKey(String apiKey) {
            this.apiKey = apiKey;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }
    }


    class SelfInfo{
        private Location location;

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }

    class Location{
        private String city;
        private String province;
        private String street;

        public Location() {
        }

        public Location(String city) {
            this.city = city;
        }

        public String getCity() {
            return city;
        }

        public String getProvince() {
            return province;
        }

        public String getStreet() {
            return street;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    class InputUrl{
        private String url;

        public InputUrl() {
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    class InputText {
        private String text;

        public InputText() {
        }

        public InputText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
    class Perception{
        private InputText inputText;
        private InputUrl inputImage;
        private InputUrl inputMedia;
        private SelfInfo selfInfo;

        public Perception(String inputText) {
            this.inputText = new InputText(inputText);
        }

        public Perception(String inputText, SelfInfo selfInfo) {
            this.inputText = new InputText(inputText);
            this.selfInfo = selfInfo;
        }

        public Perception() {
        }

        public InputText getInputText() {
            return inputText;
        }

        public void setInputText(InputText inputText) {
            this.inputText = inputText;
        }

        public InputUrl getInputImage() {
            return inputImage;
        }

        public void setInputImage(InputUrl inputImage) {
            this.inputImage = inputImage;
        }

        public InputUrl getInputMedia() {
            return inputMedia;
        }

        public void setInputMedia(InputUrl inputMedia) {
            this.inputMedia = inputMedia;
        }

        public SelfInfo getSelfInfo() {
            return selfInfo;
        }

        public void setSelfInfo(SelfInfo selfInfo) {
            this.selfInfo = selfInfo;
        }
    }

    public static void main(String[] args) {
        RobotChat robotChat = new RobotChat();
        robotChat.chat("怎么这么复杂了");
    }
}
