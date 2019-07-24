package com.sanri.algorithm;

/**
 * 
 * 创建时间:2016-10-2下午1:21:36<br/>
 * 创建者:sanri<br/>
 * 功能:仿QQ活跃天数计算<br/>
 */
public class BriskDays {
	/**
	 * 
	 * 功能:根据时长毫秒数计算出活跃天数,当天的延时,上限为两个活跃天<br/>
	 * 创建时间:2016-10-2下午1:22:55<br/>
	 * 作者：sanri<br/>
	 */
	public static float calcBriskDays(long duration){
		long minute10 = 10 * 60 * 1000;
		float briskDays = 0f;
		if(duration >= minute10 && duration < 6 * minute10){
			briskDays = 0.5f;
		}else if(duration >= 6 * minute10){
			long sub = duration - 6 * minute10;
			briskDays = 1f + sub/minute10 * 0.2f;	
			briskDays = briskDays > 2f ? 2f:briskDays;		//最多 2 个
		}
		return briskDays;
	}
	
	/*
	 * 
	 * 根据活跃天数计算出等级
	 * d = n^2+4n	d 为活跃天数   n 为等级
	 * 升到下一级所需天数为 2n +5
	 */
	public static int calcLevel(float briskDays){
		int level = 0;
		for (int i = 1; i <= 63; i++) {
			if(i * i + 4 * i > briskDays){
				level = i - 1;
				break;
			}
		}
//		System.out.println("level:"+level);
		return level + 1 ;
	}
	
	public static void main(String[] args) {
//		System.out.println(new Date().getTime() - System.currentTimeMillis());
		System.out.println(calcLevel(12));
		System.out.println(calcBriskDays(3434223));
	}
}
