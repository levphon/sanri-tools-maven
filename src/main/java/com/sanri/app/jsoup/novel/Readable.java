package com.sanri.app.jsoup.novel;

import java.io.IOException;
import java.util.List;

/**
 * 创建人     : sanri
 * 创建时间   : 2018/11/16-21:23
 * 功能       : 小说可读章节
 */
public interface Readable {

    /**
     * 列出所有章节及对应标题
     * @return
     */
    List<Chapter> chapterCatalog(Novel novel) throws IOException;

    /**
     * 查询最新 10 章,用于刷新最新章节; 轻量级刷新
     * @param novel
     * @return
     * @throws IOException
     */
    List<Chapter> newest10Chapter(Novel novel) throws IOException;

    /**
     * 查询某一章节内容
     * @param chapter
     * @return
     */
    String content(Novel novel,Chapter chapter) throws IOException;

}
