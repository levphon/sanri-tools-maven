package com.sanri.app.postman;

import java.util.List;


/**
 * 
 * 创建时间:2017-5-28下午3:42:52<br/>
 * 创建者:sanri<br/>
 * 功能:sql 客户端文件列表,返回结果对象<br/>
 */
public class FileListResult {
	private List<FileInfo>  files;
	private int total;
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public List<FileInfo> getFiles() {
		return files;
	}
	public void setFiles(List<FileInfo> files) {
		this.files = files;
	}
}
