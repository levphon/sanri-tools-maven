package com.sanri.app.xsd.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.QName;

import sanri.utils.DOMUtil;

import com.sanri.app.xsd.XsdContext;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-17下午6:17:37<br/>
 * 功能:xsd 简单元素  <br/>
 */
public class XsdSimpleType extends XsdType{
	public XsdSimpleType(Element element) {
		super(element);
	}
	//判断是否枚举
	private boolean enumeration;
	//枚举的所有值
	private List<String> enumerationValues = new ArrayList<String>();

	@SuppressWarnings("unchecked")
	@Override
	public void parse() {
		super.parse();
		Element restrictionElement = element.element(new QName(XsdContext.RESTRICTION_TYPE_NAME,namespace));
		if(restrictionElement != null){
			String baseType = DOMUtil.elementAttrValue(restrictionElement, "base");
			this.name = baseType;
			
			//解析是不是枚举
			Iterator<Element> enumerationElementIterator = restrictionElement.elementIterator(new QName(XsdContext.ENUMERATION_TYPE_NAME,namespace));
			if(enumerationElementIterator.hasNext()){
				this.enumeration = true;
			}
			//添加枚举值
			while(enumerationElementIterator.hasNext()){
				Element enumerationElement = enumerationElementIterator.next();
				String value = DOMUtil.elementAttrValue(enumerationElement, "value");
				enumerationValues.add(value);
			}
		}
	}

	public boolean isEnumeration() {
		return enumeration;
	}

	public List<String> getEnumerationValues() {
		return enumerationValues;
	}

}
