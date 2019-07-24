package com.sanri.deginmodel.factory;

/**
 * 
 * 创建时间:2017-9-2下午5:50:08<br/>
 * 创建者:sanri<br/>
 * 功能:沙尘暴<br/>
 */
public class Broom implements Moveable {

	@Override
	public void run() {
		System.out.println("一路沙尘暴飞奔而来broom.....");
	}

}
