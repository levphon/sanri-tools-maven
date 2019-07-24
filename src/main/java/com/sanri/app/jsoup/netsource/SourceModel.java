package com.sanri.app.jsoup.netsource;

/**
 * 网络资源
 */
public class SourceModel {
    private String title;
    private String panUrl;
    private String shareTime;
    private String source;
    private String shareMan;
    private String fileSize;
    private String recordTime;
    private String visits;

    public SourceModel() {
    }

    public SourceModel(String title) {
        this.title = title;
    }

    public SourceModel(String title, String panUrl) {
        this.title = title;
        this.panUrl = panUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getPanUrl() {
        return panUrl;
    }

    public String getShareTime() {
        return shareTime;
    }

    public String getSource() {
        return source;
    }

    public String getShareMan() {
        return shareMan;
    }

    public String getFileSize() {
        return fileSize;
    }

    public String getRecordTime() {
        return recordTime;
    }

    /**
     * 配置相关属性
     * @param source
     * @param shareMan
     * @param fileSize
     * @param shareTime
     * @param visits
     * @param recordTime
     */
    public void config(String source,String shareMan,String fileSize,String shareTime,String visits,String recordTime){
        this.source = source;
        this.shareMan = shareMan;
        this.fileSize = fileSize;
        this.shareTime = shareTime;
        this.visits = visits;
        this.recordTime = recordTime;
    }

    public void setPanUrl(String panUrl) {
        this.panUrl = panUrl;
    }

    @Override
    public String toString() {
        return title +" "+panUrl + " "+fileSize;
    }
}
