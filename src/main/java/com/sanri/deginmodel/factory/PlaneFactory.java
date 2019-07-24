package com.sanri.deginmodel.factory;

/**
 * 
 * 创建时间:2017-9-2下午5:48:41<br/>
 * 创建者:sanri<br/>
 * 功能:飞机工厂,生成飞机<br/>
 */
public class PlaneFactory extends VehicleFactory{
	public Moveable create() {
		return new Plane();
	}
}
