package com.sanri.wechat.messages;

import java.util.List;
/**
 * 图文消息
 * @author Majinglei
 *
 */
public class NewsMessage extends WechatMessage {

	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<Article> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<Article> getArticles() {
		return Articles;
	}

	public void setArticles(List<Article> articles) {
		Articles = articles;
	}

	public static final class Article {
		// 图文消息名称
		private String Title;
		// 图文消息描述
		private String Description;
		// 图片链接，支持JPG、PNG格式，较好的效果为大图640*320，小图80*80，限制图片链接的域名需要与开发者填写的基本资料中的Url一致
		private String PicUrl;
		// 点击图文消息跳转链接
		private String Url;

		public String getTitle() {
			return Title;
		}

		public void setTitle(String title) {
			Title = title;
		}

		public String getDescription() {
			return Description;
		}

		public void setDescription(String description) {
			Description = description;
		}

		public String getPicUrl() {
			return PicUrl;
		}

		public void setPicUrl(String picUrl) {
			PicUrl = picUrl;
		}

		public String getUrl() {
			return Url;
		}

		public void setUrl(String url) {
			Url = url;
		}

		@Override
		public String toString() {
			return "Article [Description=" + Description + ", PicUrl=" + PicUrl
					+ ", Title=" + Title + ", Url=" + Url + "]";
		}
	}

	@Override
	public String toXML() {
		return null;
	}
}
