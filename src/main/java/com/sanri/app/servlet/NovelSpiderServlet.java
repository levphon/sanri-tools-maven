package com.sanri.app.servlet;

import com.sanri.app.BaseServlet;
import com.sanri.app.jsoup.novel.Chapter;
import com.sanri.app.jsoup.novel.Novel;
import com.sanri.app.jsoup.novel.NovelConfigManager;
import com.sanri.app.jsoup.novel.NovelNetSource;
import com.sanri.frame.RequestMapping;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * 创建人     : sanri
 * 创建时间   : 2018/11/16-21:07
 * 功能       : 小说抓取器
 */
@RequestMapping("/novel")
public class NovelSpiderServlet extends BaseServlet {

    //保存最近查看的 10 本书
    private Queue<Novel> latestUse = new ArrayDeque<Novel>(10);

    public Queue<Novel> latestUse(){
        return latestUse;
    }

    /**
     * 列出所有网源
     * @return key==>Novel
     */
    public Map<String,NovelNetSource> listNetSources(){
        return NovelConfigManager.novelNetSourceMap;
    }

    /**
     * 搜索书箱
     * @param netSource
     * @param keyword
     * @return
     */
    public List<Novel> searchBook(String netSource,String keyword) throws IOException {
        NovelNetSource novelNetSource = NovelConfigManager.getNovelNetSource(netSource);
        List<Novel> search = novelNetSource.search(keyword);
        return search;
    }

    /**
     * 列出章节目录
     * @param netSource
     * @param novel
     * @return
     */
    public List<Chapter> listChapters(String netSource,Novel novel) throws IOException {
        if(!latestUse.contains(novel)){
            //添加最近使用
            latestUse.add(novel);
        }
        NovelNetSource novelNetSource = NovelConfigManager.getNovelNetSource(netSource);
        if(novelNetSource == null){
            logger.error("没有找到网源:"+netSource);
            return null;
        }
        List<Chapter> chapters = novelNetSource.chapterCatalog(novel);
        return chapters;
    }

    /**
     * 列出章节内容
     * @param netSource
     * @param chapter
     * @return
     */
    public String contentHtml(String netSource,Novel novel,Chapter chapter) throws IOException {
        NovelNetSource novelNetSource = NovelConfigManager.getNovelNetSource(netSource);
        String content = novelNetSource.content(novel,chapter);
        return content;
    }

}
