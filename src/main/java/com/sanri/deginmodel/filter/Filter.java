package com.sanri.deginmodel.filter;


/**
 * 作者: sanri
 * 时间: 2017/07/30 19:21
 * 功能: 过滤器接口
 */
public interface Filter {
    void doFilter(Request req, Response resp, FilterChain filterChain);
}
