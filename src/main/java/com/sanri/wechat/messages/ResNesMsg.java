package com.sanri.wechat.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResNesMsg extends ResBaseMsg implements Serializable{

	private static final long serialVersionUID = 6827780381613993229L;

	private News news;
	
	public News getNews() {
		return news;
	}
	public void setNews(News news) {
		this.news = news;
	}
	public static class News implements Serializable{
		private static final long serialVersionUID = -3007357232195953286L;
		private List<Articles> articles = new ArrayList<Articles>();
		public List<Articles> getArticles() {
			return articles;
		}
	}
	public static class Articles implements Serializable{
		private static final long serialVersionUID = -7529262233583644444L;
		//标题
		private String title;
		//描述
		private String description;
		//点击后跳转的链接
		private String url;
		//图文消息的图片链接，支持 JPG、PNG 格式，较好的效果为大图 640*320，小图 80*80
		private String picurl;

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getPicurl() {
			return picurl;
		}

		public void setPicurl(String picurl) {
			this.picurl = picurl;
		}
	}
}
