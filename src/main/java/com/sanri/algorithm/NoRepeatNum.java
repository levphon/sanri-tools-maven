package com.sanri.algorithm;

public class NoRepeatNum {

	 
	private static int id = 0;
	
	private static Object obj = new Object();
	
	private static int  serverId = 0;
	/**
	 * 获得唯一id
	 * @return id
	 */
	public static long getId(){
		synchronized (obj) {
			id = id + 1;
			return (((long)( serverId & 0xFFFF)) << 48) | (((System.currentTimeMillis() / 1000) & 0x00000000FFFFFFFFl) << 16) | (id & 0x0000FFFF);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(getId());
	}

}
