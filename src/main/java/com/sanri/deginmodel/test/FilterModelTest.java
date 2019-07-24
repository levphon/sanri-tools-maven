package com.sanri.deginmodel.test;

import com.sanri.deginmodel.filter.*;
import org.junit.Test;

/**
 * 作者: sanri
 * 时间: 2017/08/03 10:34
 * 功能: 过滤器模式测试
 */
public class FilterModelTest {

    @Test
    public void testFilter(){
        String msg="<script>, 敏感， :),你好吗";
        Request req = new Request();
        req.setReqStr(msg);
        Response resp = new Response();
        FilterChain fc = new FilterChain();
        fc.addFilter(new HtmlFilter()).
                addFilter(new SensiveFilter());

        fc.doFilter(req, resp, fc);
        System.out.println(resp.getRespStr());
    }
}
