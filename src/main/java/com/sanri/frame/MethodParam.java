package com.sanri.frame;

import java.lang.reflect.Method;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-4-19下午5:37:22<br/>
 * 功能:方法参数 <br/>
 */
public class MethodParam {
	private Class<?> paramClazz;
	private String name;
	private Object value;
	private Method method;
	private Class<?> clazz;
	
	public Class<?> getParamClazz() {
		return paramClazz;
	}
	public void setParamClazz(Class<?> paramClazz) {
		this.paramClazz = paramClazz;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
}
