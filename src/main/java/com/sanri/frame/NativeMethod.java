package com.sanri.frame;

import java.lang.reflect.Method;
import java.util.List;

import javassist.CtMethod;

public class NativeMethod {
	private Class<?> clazz;
	private Method method;
	private Class<?> returnType;
	private CtMethod ctMethod;
	private List<MethodParam> methodParams;
	
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	public Class<?> getReturnType() {
		return returnType;
	}
	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	
	@Override
	public String toString() {
		StringBuffer args = new StringBuffer();
		if(methodParams != null && !methodParams.isEmpty()){
			boolean first = true;
			for (MethodParam methodParam:methodParams) {
				Class<?> paramClazz = methodParam.getParamClazz();
				String name = methodParam.getName();
				if(!first){
					args.append(",");
				}else{
					first = false;
				}
				args.append(paramClazz.getSimpleName()+" "+name);
			}
		}
		return this.returnType.getName()+" "+this.clazz.getName()+"."+this.method.getName()+"("+args+")";
	}
	public CtMethod getCtMethod() {
		return ctMethod;
	}
	public void setCtMethod(CtMethod ctMethod) {
		this.ctMethod = ctMethod;
	}
	public List<MethodParam> getMethodParams() {
		return methodParams;
	}
	public void setMethodParams(List<MethodParam> methodParams) {
		this.methodParams = methodParams;
	}
}
