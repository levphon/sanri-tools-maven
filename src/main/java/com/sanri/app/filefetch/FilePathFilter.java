package com.sanri.app.filefetch;

import java.io.File;
import java.util.Map;

public interface FilePathFilter {
	Map<String,File> mappingPkgSourcePath(String handlePath) throws IllegalArgumentException;
	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2018-4-26下午2:13:26<br/>
	 * 功能:加载配置 <br/>
	 * @param config
	 */
	void setConfig(Map<String, String> config);
}
