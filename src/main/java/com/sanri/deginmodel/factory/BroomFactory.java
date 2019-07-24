package com.sanri.deginmodel.factory;

/**
 * 
 * 创建时间:2017-9-2下午5:50:23<br/>
 * 创建者:sanri<br/>
 * 功能:生产沙尘暴的工厂<br/>
 */
public class BroomFactory extends VehicleFactory{
	public Moveable create() {
		return new Broom();
	}
}
