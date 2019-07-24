package com.sanri.app.servlet;

import com.sanri.app.BaseServlet;
import com.sanri.app.jsoup.netsource.PageResult;
import com.sanri.app.jsoup.netsource.PansosoSpider;
import com.sanri.app.jsoup.netsource.SourceModel;
import com.sanri.frame.RequestMapping;

import java.io.IOException;

@RequestMapping("/netsource")
public class NetSourceServlet extends BaseServlet {
    PansosoSpider pansosoSpider = new PansosoSpider();

    /**
     * 搜索结果
     * @param keyword
     * @param page
     * @return
     */
    public PageResult search(String keyword,int page) throws IOException {
        PageResult<SourceModel> sourceModelPageResult = pansosoSpider.searchResource(keyword, page);
        return sourceModelPageResult;
    }
}
