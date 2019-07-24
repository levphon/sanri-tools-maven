package com.sanri.deginmodel.filter;

public class HtmlFilter implements Filter {

	@Override
	public void doFilter(Request req, Response resp, FilterChain filterChain) {
		System.out.println("req---HtmlFilter");
		req.setReqStr(req.getReqStr().replace("<", "[").replace(">", "]"));
		filterChain.doFilter(req, resp, filterChain);
		resp.setRespStr("htmlFilter");
		System.out.println("resp---HtmlFilter");
	}

}
