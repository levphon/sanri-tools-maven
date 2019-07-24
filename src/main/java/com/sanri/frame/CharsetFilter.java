package com.sanri.frame;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-4-17下午1:50:35<br/>
 * 功能:解决跨域和字符集问题 <br/>
 */
public class CharsetFilter implements Filter {
	private String encoding;
	private boolean forceEncoding;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) servletresponse;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

		//设置字符集
		if (encoding != null && (forceEncoding || servletrequest.getCharacterEncoding() == null)) {
			servletrequest.setCharacterEncoding(encoding);
			if (forceEncoding){
				response.setCharacterEncoding(encoding);
//				response.setContentType("text/html;charset=UTF-8");
			}
		}
		filterchain.doFilter(servletrequest, response);
	}

	@Override
	public void init(FilterConfig filterconfig) throws ServletException {

	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public void setForceEncoding(boolean forceEncoding) {
		this.forceEncoding = forceEncoding;
	}

}
