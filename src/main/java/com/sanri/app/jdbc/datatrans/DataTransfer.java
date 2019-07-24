package com.sanri.app.jdbc.datatrans;

import java.util.List;
import java.util.Map;

import com.sanri.app.jdbc.Table;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-4-21下午3:31:46<br/>
 * 功能:实现些接口来实现数据转移<br/>
 * 注:建议写入 com.sanri.app.datatransfer.impl 中
 */
public interface DataTransfer {
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2017-7-22下午4:15:32<br/>
	 * 功能:数据转移处理 <br/>
	 * @param data 需要处理的数据
	 * @param table 源表结构 
	 */
	void handler(List<Map<String, String>> data, Table table);
}
