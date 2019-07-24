package com.sanri.app.ocr;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import sanri.utils.HttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sanri.app.translate.YoudaoEnglishTranslate.getDigest;

public class YoudaoOcr {
    private static final String YOUDAO_URL = "https://openapi.youdao.com/ocrapi";

    private static final String APP_KEY = "28a343197650d05a";

    private static final String APP_SECRET = "hioXXfQjbWnRDuLrvbvx3tGpo7hDUpHt";


    public List<String> resolve(InputStream inputStream) throws IOException, IllegalAccessException {
        //转流数据为 base64
        byte[] bytes = IOUtils.toByteArray(inputStream);
        byte[] encodeBase64 = Base64.encodeBase64(bytes);
        String base64 = new String(encodeBase64);

        String salt = String.valueOf(System.currentTimeMillis());
        String detectType = "10012";
        String imageType = "1";
        String langType = "zh-en";

        Map<String,String> params = new HashMap<String,String>();
        params.put("detectType", detectType);
        params.put("imageType", imageType);
        params.put("langType", langType);
        params.put("img", base64);
        params.put("docType", "json");
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(base64) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("salt", salt);
        params.put("sign", sign);

        String result = HttpUtil.postFormData(YOUDAO_URL, params);
        JSONObject resultObject = JSONObject.parseObject(result);
        int errorCode = resultObject.getIntValue("errorCode");
        if(errorCode != 0){
            throw new IllegalAccessException("访问有道 ocr 识别 ["+YOUDAO_URL+"]出错,错误码:"+errorCode);
        }

        List<String> linesList = new ArrayList<String>();
        JSONArray lines = resultObject.getJSONObject("Result").getJSONArray("regions").getJSONObject(0).getJSONArray("lines");
        if(lines != null && !lines.isEmpty()){
            for (int i = 0; i < lines.size(); i++) {
                JSONObject line = lines.getJSONObject(i);
                String text = line.getString("text");
                linesList.add(text);
            }
        }
        return linesList;
    }

     public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}
