package com.sanri.frame;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HandlerExceptionResolver {
	ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Throwable ex);
}
