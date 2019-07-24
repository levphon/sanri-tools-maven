package com.sanri.deginmodel.filter;

public class SensiveFilter implements Filter {

	@Override
	public void doFilter(Request req, Response resp, FilterChain filterChain) {
		req.setReqStr(req.getReqStr().replace("敏感", ""));
		System.out.println("req---sensiveFilter");
		filterChain.doFilter(req, resp, filterChain);
		resp.setRespStr("sensiveFilter");
		System.out.println("resp---sensiveFilter");
	}

}
