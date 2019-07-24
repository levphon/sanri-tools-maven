package com.sanri.app.wscall;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-6-21上午11:15:33<br/>
 * 功能: webservice 类型 <br/>
 */
public class WsdlType {
	//类型名称
	private String typeName;
	private boolean simple;
	private List<WsdlParam> childParams;
	
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public boolean isSimple() {
		return simple;
	}
	public void setSimple(boolean simple) {
		this.simple = simple;
	}
	public List<WsdlParam> getChildParams() {
		return childParams;
	}
	public void setChildParams(List<WsdlParam> childParams) {
		this.childParams = childParams;
	}
	
	public void addChildParam(WsdlParam wsdlParam){
		if(childParams == null){
			childParams = new ArrayList<WsdlParam>();
		}
		this.childParams.add(wsdlParam);
	}
	@Override
	public String toString() {
		return JSONObject.toJSONString(this);
	}
}
