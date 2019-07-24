package com.sanri.app.jsoup.novel;

/**
 * 创建人     : sanri
 * 创建时间   : 2018/11/16-21:29
 * 功能       :
 */
public abstract class NovelNetSource implements Searchable,Readable {
    protected String name;
    protected String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
