package com.sanri.app.jsoup.novel;

import java.io.IOException;
import java.util.List;

/**
 * 创建人     : sanri
 * 创建时间   : 2018/11/16-21:15
 * 功能       : 可搜索小说
 */
public interface Searchable {

    /**
     * 小说搜索
     * @param keyword
     * @return
     */
    List<Novel> search(String keyword) throws IOException;

}
