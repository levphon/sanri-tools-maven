package com.sanri.app.sqlparser;

import net.sf.jsqlparser.JSQLParserException;

public interface ParserItem {
    /**
     * 解析当前组件
     */
    void parser() throws JSQLParserException;
}
