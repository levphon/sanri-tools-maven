package com.sanri.app.xsd.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.QName;

import com.sanri.app.xsd.XsdContext;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-17下午9:25:04<br/>
 * 功能:xsd 选择元素 <br/>
 */
public class XsdChoiceElement extends XsdNode{
	private List<XsdElement> xsdElements = new ArrayList<XsdElement>();
	
	public XsdChoiceElement(Element element) {
		super(element);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void parse() {
		super.parse();
		
		Iterator<Element> elementIterator = element.elementIterator(new QName(XsdContext.ELEMENT_NAME,namespace));
		while(elementIterator.hasNext()){
			Element elementElement = elementIterator.next();
			XsdElement xsdElement = new XsdElement(elementElement);
			xsdElement.setNamespace(namespace);
			xsdElement.setXsdContext(xsdContext);
			xsdElement.parse();
			this.xsdElements.add(xsdElement);
		}
	}

	public List<XsdElement> getXsdElements() {
		return xsdElements;
	}
}
