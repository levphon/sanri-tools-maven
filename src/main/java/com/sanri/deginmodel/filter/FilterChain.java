package com.sanri.deginmodel.filter;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者: sanri
 * 时间: 2017/07/30 19:23
 * 功能: 过滤器链
 */
public class FilterChain implements Filter{
    private List<Filter> filters = new ArrayList<Filter>();
    private int index;         //当前是第几个过滤器,默认为 0
    public FilterChain addFilter(Filter filter){
        filters.add(filter);
        return this;
    }

    @Override
    public void doFilter(Request req, Response resp, FilterChain filterChain) {
        if(filters.size() ==0 || index == filters.size()){
            return ;
        }
        Filter filter = filters.get(index);
        index ++;
        filter.doFilter(req,resp,filterChain);
    }
}
