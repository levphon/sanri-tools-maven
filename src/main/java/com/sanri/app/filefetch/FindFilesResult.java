package com.sanri.app.filefetch;

import java.util.List;

public class FindFilesResult {
	private String filename;
	private List<String> errorFiles;
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public List<String> getErrorFiles() {
		return errorFiles;
	}
	public void setErrorFiles(List<String> errorFiles) {
		this.errorFiles = errorFiles;
	}
}
