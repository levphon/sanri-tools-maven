package com.sanri.app.ocr;

import sanri.utils.HttpUtil;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * 抓取别人的 api 接口
 */
public class WebApis {
    private static final String ocrUrl = "http://tool.mkblog.cn/ocr/api.php";

    /**
     * 孟坤工具箱的 ocr 识别
     * @param inputStream
     * @return
     */
    public List<String> ocr(InputStream inputStream) {
        String result = HttpUtil.postStream(ocrUrl, "file", inputStream);
        String[] split = result.split("\r");
        return Arrays.asList(split);
    }
}
