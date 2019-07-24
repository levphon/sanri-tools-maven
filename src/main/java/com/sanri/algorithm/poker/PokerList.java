package com.sanri.algorithm.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sanri.utils.RegexValidate;

public class PokerList extends ArrayList<Poker>{
	AnalyseResult analyseResult = new AnalyseResult();
	class AnalyseResult{
		int fourCount;
		int threeCount;
		int doubleCount;
		int singleCount;
		
		List<Poker> fourCountData = new ArrayList<Poker>();
		List<Poker> threeCountData = new ArrayList<Poker>();
		List<Poker> doubleCountData = new ArrayList<Poker>();
		List<Poker> singleCountData = new ArrayList<Poker>();
	}
	
	@Override
	public boolean add(Poker e) {
		boolean bool = super.add(e);
		
		return bool;
	}
	/**
	 * 
	 * 功能：分析牌组合,<br/>
	 * 输入参数：<br/>
	 * 输出参数：<br/>
	 * 作者:sanri<br/>
	 * 创建时间：2016年5月18日下午12:01:51<br/>
	 * @return 
	 */
	public AnalyseResult analyse(){
		Collections.sort(this);
		if(RegexValidate.isEmpty(this)){return analyseResult;}
		for(int i=0;i<this.size();i++){
			int sameCount = 1,pokerValue = this.get(i).getValue();
			List<Poker> currentList = new ArrayList<Poker>();
			currentList.add(this.get(i));
			int j = 0;
			for(j=i+1;j<this.size();j++){
				if(this.get(j).getValue() == pokerValue){
					sameCount ++;
					currentList.add(this.get(j));
					continue;
				}
				i = j - 1;
				break;
			}
			switch(sameCount){
			case 1:
				analyseResult.singleCount ++;
				analyseResult.singleCountData.addAll(currentList);
				break;
			case 2:
				analyseResult.doubleCount++;
				analyseResult.doubleCountData.addAll(currentList);
				break;
			case 3:
				analyseResult.threeCount++;
				analyseResult.threeCountData.addAll(currentList);
				break;
			case 4:
				analyseResult.fourCount++;
				analyseResult.fourCountData.addAll(currentList);
				break;
			default:
				System.out.println("出老千啊!");
			}
			if(j == this.size() ){
				//里层循环如果到了末尾,则不需要继续比较了 important
				break;
			}
		}
		return analyseResult;
	}
	/**
	 * 
	 * 功能:返回所有牌的面值符号字符串格式为 poker1,poker2,poker3...<br/>
	 * 创建时间:2016-5-10上午11:06:12<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * 
	 * @return<br/>
	 */
	public String pokerSymbols() {
		if (!RegexValidate.isEmpty(this)) {
			StringBuffer sb = new StringBuffer();
			for (Poker poker:this) {
				sb.append(",").append(poker.getSymbol());
			}
			return sb.substring(1);
		}
		return "";
	}

	/**
	 * 
	 * 功能:返回所有牌的面值字符串格式为p1,p2,p3...<br/>
	 * 创建时间:2016-5-10上午11:09:18<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * 
	 * @return<br/>
	 */
	public String pokerValues() {
		if (!RegexValidate.isEmpty(this)) {
			StringBuffer sb = new StringBuffer();
			for (Poker poker:this) {
				sb.append(",").append(poker.getValue());
			}
			return sb.substring(1);
		}
		return "";
	}
	/**
	 * 
	 * 功能:返回扑克值的数组 <br/>
	 * 创建时间:2016-5-15下午8:53:30<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @return<br/>
	 */
	public int[] pokerValuesArray() {
		if (this.isEmpty()) {
			return null;
		}
		int[] array = new int[this.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = this.get(i).getValue();
		}
		return array;
	}
	/**
	 * 
	 * 功能:返回扑克符号的数组<br/>
	 * 创建时间:2016-5-15下午8:53:47<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @return<br/>
	 */
	public String[] pokerSymbolArray() {
		if (this.isEmpty()) {
			return null;
		}
		String[] array = new String[this.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = this.get(i).getSymbol();
		}
		return array;
	}

}
