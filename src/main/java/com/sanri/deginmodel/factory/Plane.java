package com.sanri.deginmodel.factory;

/**
 * 
 * 创建时间:2017-9-2下午5:48:22<br/>
 * 创建者:sanri<br/>
 * 功能:飞机<br/>
 */
public class Plane implements Moveable {

	@Override
	public void run() {
		System.out.println("飞机飞行....");
	}

}
