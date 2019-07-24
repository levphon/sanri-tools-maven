package com.sanri.app.xsd.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.QName;

import sanri.utils.DOMUtil;

import com.sanri.app.xsd.XsdContext;
import com.sanri.app.xsd.XsdLoader;

/**
 * 
 * 作者:sanri <br/>
 * 时间:2017-8-17下午11:19:14<br/>
 * 功能:xsd 复杂类型 <br/>
 */
public class XsdComplexType extends XsdType{
	public XsdComplexType(Element element) {
		super(element);
	}
	//子元素序列
	private List<XsdElement> sequence = new ArrayList<XsdElement>();
	//从元素列表中选一个
	private XsdChoiceElement xsdChoiceElement;
	//类型节点属性
	private List<XsdAttribute> attributes = new ArrayList<XsdAttribute>();
	private XsdType base;
	
	@Override
	public void parse() {
		super.parse();
		if(xsdContext.getTypeMap().containsKey(this.name)){
			//已经解析过无需再次解析
			return ;
		}
		//如果复杂类型还包了一层,则先把那层解析出来
		Element complexTypeContentElement = element.element(new QName(XsdContext.COMPLEX_TYPE_CONTENT_NAME,namespace));
		Element complexTypeElement = element;
		if(complexTypeContentElement != null){
			complexTypeElement = complexTypeContentElement;
		}
		//如果复杂类型有继承,解析继承
		Element extensionElement = complexTypeElement.element(new QName(XsdContext.EXTENSION_NAME,namespace));
		if(extensionElement != null){
			parseChildElement(extensionElement);			//如果有继承的元素,则扩展的序列,属性需要在继承元素中查找 
			String typeName = DOMUtil.elementAttrValue(extensionElement, "base");
			boolean containsKey = xsdContext.getTypeMap().containsKey(typeName);
			XsdType baseXsdType = null;
			if(!containsKey){
				Iterator<XsdContext> iterator = xsdContext.getImportMap().values().iterator();
				while(iterator.hasNext()){
					XsdContext importXsdContext = iterator.next();
					if(importXsdContext.getTypeMap().containsKey(typeName)){
						baseXsdType = importXsdContext.getTypeMap().get(typeName);
						break;
					}
				}
			}else{
				baseXsdType = xsdContext.getTypeMap().get(typeName);
			}
			
//			logger.warn("未找到继承的元素类型:"+typeName+",将从本文档中找到并解析,文档地址:"+xsdContext.getXsdUrl());
			baseXsdType = xsdContext.parseComplexType(typeName);
			if(baseXsdType == null){
				logger.error("未找到继承的元素类型:"+typeName+",文档地址:"+xsdContext.getXsdUrl());
				//未找到类型
				return ;
			}
			
			this.base = baseXsdType;
			return ;
		}
		parseChildElement(complexTypeElement);
	}
	/**
	 * 功能:<br/>
	 * 创建时间:2017-8-19下午9:09:03<br/>
	 * 作者：sanri<br/>
	 * @param complexTypeElement<br/>
	 */
	@SuppressWarnings("unchecked")
	private void parseChildElement(Element complexTypeElement) {
		//如果复杂类型有子元素序列,解析子元素序列
		Element sequenceElement = complexTypeElement.element(new QName(XsdContext.SEQUENCE_NAME, namespace));
		if(sequenceElement != null){
			//元素类型
			Iterator<Element> elementIterator = sequenceElement.elementIterator(new QName(XsdContext.ELEMENT_NAME,namespace));
			while(elementIterator.hasNext()){
				Element elementElement = elementIterator.next();
				XsdElement xsdElement = new XsdElement(elementElement);
				xsdElement.setXsdContext(xsdContext);
				xsdElement.setNamespace(namespace);
				xsdElement.parse();
				this.sequence.add(xsdElement);
			}
			//除了有元素类型,还有 choice 类型  ,默认只会有一个 choice ,如果有多个 choice ,需要更改这里
			Element choiceElement = sequenceElement.element(new QName(XsdContext.CHOICE_NAME,namespace));
			if(choiceElement != null){
				XsdChoiceElement xsdChoiceElement = new XsdChoiceElement(choiceElement);
				xsdChoiceElement.setNamespace(namespace);
				xsdChoiceElement.setXsdContext(xsdContext);
				xsdChoiceElement.parse();
				this.xsdChoiceElement = xsdChoiceElement;
			}
			
		}
		//如果复杂类型有属性列表,解析属性
		Iterator<Element> attributeElementIterator = complexTypeElement.elementIterator(new QName(XsdContext.ATTRIBUTE_NAME,namespace));
		while(attributeElementIterator.hasNext()){
			Element attributeElement = attributeElementIterator.next();
			XsdAttribute xsdAttribute = new XsdAttribute(attributeElement);
			xsdAttribute.setNamespace(namespace);
			xsdAttribute.setXsdContext(xsdContext);
			xsdAttribute.parse();
			this.attributes.add(xsdAttribute);
		}
	}
	public XsdChoiceElement getXsdChoiceElement() {
		return xsdChoiceElement;
	}
	public XsdType getBase() {
		return base;
	}
	public List<XsdElement> getSequence() {
		return sequence;
	}
	public List<XsdAttribute> getAttributes() {
		return attributes;
	}
}
