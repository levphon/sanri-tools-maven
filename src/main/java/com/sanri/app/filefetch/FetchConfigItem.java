package com.sanri.app.filefetch;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * 作者:sanri <br/>
 * 时间:2018-5-8下午2:36:36<br/>
 * 功能:文件抓取配置项 <br/>
 */
public class FetchConfigItem {
	private String project;
	private String sourcePath;
	
	// 编译后到 WEB-INF/classpath 路径 
	private List<String> classpaths = new ArrayList<String>();
	// 编译后到 root 路径 
	private List<String> resourcesPaths = new ArrayList<String>();
	
	public final static String CLASS_PATH = "WEB-INF/classes";
	
	public FetchConfigItem(String project, String sourcePath) {
		super();
		this.project = project;
		this.sourcePath = sourcePath;
	}

	/**
	 * 
	 * 作者:sanri <br/>
	 * 时间:2018-5-9上午10:30:48<br/>
	 * 功能:解析配置项 <br/>
	 * @param itemProperties
	 * @return
	 */
	public static FetchConfigItem parser(ItemProperties itemProperties) {
		if(itemProperties == null)return null;
		
		String project = itemProperties.getString("project");
		String sourcepath = itemProperties.getString("sourcepath");
		FetchConfigItem fetchConfigItem  = new FetchConfigItem(project, sourcepath);
		fetchConfigItem.classpaths =  itemProperties.getList("compile.classpath");
		fetchConfigItem.resourcesPaths = itemProperties.getList("compile.resource");
		
		return fetchConfigItem;
	}

	public String getProject() {
		return project;
	}

	public String getSourcePath() {
		return sourcePath;
	}
	
	public void addClassPath(String classpath){
		this.classpaths.add(classpath);
	}
	
	public void addResourcePath(String resourcePath){
		this.resourcesPaths.add(resourcePath);
	}
	
	public boolean isClassPath(String path){
		for (String classpath : classpaths) {
			if(path.startsWith(classpath)){
				return true;
			}
		}
		return false;
	}
	public boolean isResourcepath(String path){
		for (String resourcePath : resourcesPaths) {
			if(path.startsWith(resourcePath)){
				return true;
			}
		}
		return false;
	}

	public String compilePath(String filePath) {
		for (String classpath : classpaths) {
			if(filePath.startsWith(classpath)){
				return filePath.replace(classpath, CLASS_PATH);
			}
		}
		for (String resourcePath : resourcesPaths) {
			if(filePath.startsWith(resourcePath)){
				return filePath.replace(resourcePath, "");
			}
		}
		throw new IllegalArgumentException("文件非编译路径,也非资源路径 :"+filePath);
	}
}
