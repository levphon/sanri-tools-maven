package com.sanri.frame;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-4-20上午9:59:26<br/>
 * 此类用来专门处理异常
 * 功能:如果有 retVal 就是返回原界面 json 数据,如果 url 不为空,则返回错误界面 <br/>
 */
public class ModelAndView {
	private String url;
	private Object params;
	
	public ModelAndView(){}
	
	public ModelAndView(String url){
		this.url = url;
	}
	public ModelAndView(String url,Object params){
		this.url = url;
		this.params = params;
	}
	public ModelAndView(Object params){
		this.params = params;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Object getParams() {
		return params;
	}
	public void setParams(Object params) {
		this.params = params;
	}
}
