
package com.sanri.app.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sanri.utils.RandomUtil;

import com.sanri.app.jdbc.Column;
import com.sanri.app.jdbc.Table;

/**
 * 作者:sanri <br/>
 * 时间:2017-7-14下午2:38:16<br/>
 * 功能: 大量数据随机写线程<br/>
 */
public class BigDataWriteThread implements Runnable{
	private Table table;
	private int count;
	private Map<String,String> fixedValMap ;
	private Log logger = LogFactory.getLog(getClass());

	public BigDataWriteThread(Table table,int count){
		this.table = table;
		this.count = count;
	}

	@Override
	public void run() {
		logger.warn("暂时还没想好");
		/**
		 * 想计算一个进度使用写文件的方式,
		 */
//		if(table != null && table.getColumns() != null && table.getColumns().size() > 0){
//			Map<String,String> data = new HashMap<String, String>();
//			for (Column column : table.getColumns()) {
//				String columnName = column.getColumnName();
//				String generatorType = column.getDataType();
//				if("idcard".equals(generatorType) ){
//					data.put(columnName, RandomUtil.idcard());
//				}else if("fixedNumeric".equals(generatorType)){
//					data.put(columnName, );
//				}else if("limitNumeric".equals(generatorType)){
//					data.put(columnName, String.valueOf(RandomUtil.randomNumber(extra_)));
//				}else if("alphabetic".equals(generatorType)){
//					data.put(columnName, RandomUtil.randomAlphabetic(extra_));
//				}else if("alphanumeric".equals(generatorType)){
//					data.put(columnName, RandomUtil.randomAlphanumeric(extra_));
//				}else if("chinese".equals(generatorType)){
//					data.put(columnName, RandomUtil.chinese(extra_, null));
//				}else if("email".equals(generatorType)){
//					data.put(columnName, RandomUtil.email(40));
//				}else if("date".equals(generatorType)){
//					data.put(columnName, RandomUtil.date(extra, null, null));
//				}else if("address".equals(generatorType)){
//					data.put(columnName, RandomUtil.address());
//				}else if("uuid".equals(generatorType)){
//					data.put(columnName, UUID.randomUUID().toString());
//				}else if("md5".equals(generatorType)){
////					data.put(columnName, MD5Util.getMD5Format(RandomUtil.random(5)));
//				}else if("select".equals(generatorType)){
//					String[] split = extra.split("\\$");
//					int randomNumber = (int) RandomUtil.randomNumber(split.length - 1);
//					data.put(columnName, split[randomNumber]);
//				}else if("fixed".equals(generatorType)){
//					data.put(columnName, extra);
//				}else{
//					logger.error("不支持的类型");
//				}
//			}
//		}
	}

	public Table getTable() {
		return table;
	}

	public int getCount() {
		return count;
	}


	public Map<String, String> getFixedValMap() {
		return fixedValMap;
	}

	public void setFixedValMap(Map<String, String> fixedValMap) {
		this.fixedValMap = fixedValMap;
	}

}
