package com.sanri.algorithm;
/**
 * 
 * 创建时间:2016-9-30下午8:26:32<br/>
 * 创建者:sanri<br/>
 * 功能:背包问题<br/>
 * 将 1 到 N 的连续整数组成的集合划分成两个子集合,且保证每个集合的数字和是相等的.
 * 例如 对于N=3 对应的集合 {1,2,3}能划分成 {3} 和 {1,2} 两个子集合
 * 当 N=7 时有 4 种划分方案
 * 问:给定任意 N 算出划分方案个数
 */
public class CollectionSplit {
	public static void getResult(int n) {
		int dyn[] = new int[100];
		int s;
		s = n * (n + 1);
		if (s % 4 == 1) {
			System.out.println(0);
		} else {
			s /= 4;
			int i, j;
			dyn[0] = 1;
			for (i = 1; i <= n; i++) {
				for (j = s; j >= i; j--)
					dyn[j] += dyn[j - i];
			}
			System.out.println(dyn[s] / 2);
		}
	}
	public static void main(String[] args) {
		getResult(7);
	}
}

