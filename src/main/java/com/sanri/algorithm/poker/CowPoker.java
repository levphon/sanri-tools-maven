package com.sanri.algorithm.poker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sanri.utils.RegexValidate;

/**
 * 
 * 创建时间:2016-5-15下午8:18:49<br/>
 * 创建者:sanri<br/>
 * 功能:用于斗牛的一组扑克,五张牌<br/>
 * 规则选用百度百科中的规则 url:http://baike.baidu.com/link?url=qdXWYwyrd8XbQYa6NQO6B66lKoNKpsB3Lvw0vY8dnp4V9kMj3m7qOuY8pa4SPYlwttTH_I1jBFxkQqrJQ8ya0a
 * <br/>
 * 根据当前的五张牌,计算出牛的等级为 <br/>
 * 0~14 分别代表 无牛,牛1~牛9,牛牛,四花,五花,五小,炸弹
 */
public class CowPoker {

	private List<Poker> pokers = new ArrayList<Poker>();
	public final static Map<Integer,String> LEVELS = new HashMap<Integer, String>();
	static{
		LEVELS.put(0, "无牛");
		LEVELS.put(10, "牛牛");
		LEVELS.put(11, "四花牛");
		LEVELS.put(12, "五花牛");
		LEVELS.put(13, "五小牛");
		LEVELS.put(14, "四炸");
		for(int i=1;i<=9;i++){
			LEVELS.put(i, "牛"+i);
		}
	}
	
	public CowPoker(){}
	public CowPoker(List<Poker> pokers){
		this.pokers = pokers;
	}
	public CowPoker(String...poker5) throws Exception{
		arrayInput(poker5);
	}
	/**
	 * 这个构造,只能输入符号,不能输入数字
	 * @param poker5
	 * @throws Exception
	 */
	public CowPoker(String poker5) throws Exception{
		try {
			String[] poker5Split = poker5.split(",");
			arrayInput(poker5Split);
		} catch (Exception e) {
			throw e;
		}
	}
	private void arrayInput(String... poker5) throws Exception {
		try {
			if(poker5.length != 5){
				throw new Exception("斗牛的牌数量必须为 5 ");
			}
			for (String pokerStr : poker5) {
				Poker poker = new Poker(pokerStr);
				this.pokers.add(poker);
			}
		} catch (Exception e) {
			throw e;
		}
	}
	
	/**
	 * 功能:输入一组牌,计算级别<br/>
	 * 创建时间:2016-5-9上午7:58:38<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @return<br/>
	 * @throws Exception 
	 */
	public Map<String,String> level() throws Exception{
		if(pokers.size() != 5){throw new Exception("斗牛牌数必须是 5 现在是:"+pokers.size());}
		Map<String,String> combination = new HashMap<String, String>();
		Collections.sort(pokers);
		String pokerSymbols = pokerSymbols();
		//用正则的话,用 逗号 不好处理,所以暂时替换成 {} 来包裹每个数字
		String wrapSymbols = "{"+pokerSymbols.replaceAll("\\,", "}{")+"}";
		//因为炸弹最大,所以先判断炸弹,再判断五小,之后是五花,四花
		if(wrapSymbols.matches("(\\{[1-9aAjqkJQK]{1}0?\\})(\\{[1-9aAjqkJQK]{1}0?\\})(\\2\\2\\2)|(\\{[1-9aAjqkJQK]{1}0?\\})(\\4\\4\\4)(\\{[1-9aAjqkJQK]{1}0?\\})")){
			combination.put(pokerSymbols, "14");
			return combination;
		}
		int gt10Count = 0;int fiveSmallCowCount = 0;boolean isFiveSmallCowPre = true;
		for (Poker poker:pokers) {
			if(poker.getValue() > 10){
				gt10Count ++;
			}
			if(poker.getValue() > 5){
				isFiveSmallCowPre = false;
			}else{
				fiveSmallCowCount += poker.getValue();
			}
		}
		//第张牌都小于5 并且之和为 10 ,则为五小
		if(isFiveSmallCowPre && fiveSmallCowCount == 10){
			combination.put(pokerSymbols, "13");
			return combination;
		}
		if(gt10Count == 4){
			//有四张花牌,四花
			combination.put(pokerSymbols, "11");
			return combination;
		}else if(gt10Count == 5){
			//有五张花牌,五花
			combination.put(pokerSymbols, "12");
			return combination;
		}
		//有牛,无牛判断
		int[] pokerValuesArray = pokerValuesArray();
		//把所有 大于 10 的值全换成 10 
		for (int i=0;i<pokerValuesArray.length;i++) {
			int pokerValue = pokerValuesArray[i];
			if(pokerValue > 10){
				pokerValuesArray[i] = 10;
			}
		}
		//实现方法暂时:三重循环
		boolean find = false;
		for(int i=0;i<pokerValuesArray.length;i++){
			for(int j=i+1;j<pokerValuesArray.length;j++){
				for(int k=j+1;k<pokerValuesArray.length;k++){
					int sum = pokerValuesArray[i] + pokerValuesArray[j] + pokerValuesArray[k];
					if(sum % 10 == 0){
						find = true;
						int [] othersPos = calcOtherPos(i,j,k);
						int remainder = (pokerValuesArray[othersPos[0]]+pokerValuesArray[othersPos[1]]) % 10;
						if(remainder == 0){
							remainder = 10;
						}
						String key = String.valueOf(pokers.get(i)+","+pokers.get(j)+","+pokers.get(k)+"#"+pokers.get(othersPos[0])+","+pokers.get(othersPos[1]));
						if(combination.get(key) == null){
							combination.put(key, String.valueOf(remainder));
						}
					}
				}
			}
		}
		if(!find){
			combination.put("spec", "0");
		}
		return combination;
	}
	/**
	 * 
	 * 功能:计算另外两张牌的位置<br/>
	 * 创建时间:2016-5-15下午9:14:33<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @param i
	 * @param j
	 * @param k
	 * @return<br/>
	 */
	private int[] calcOtherPos(int i, int j, int k) {
		int [] othersPos = new int[]{-1,-1};
		for(int m=0;m<5;m++){
			if(m != i && m != j && m!= k){
				if(othersPos[0] == -1){
					othersPos[0] = m;
				}else{
					othersPos[1] = m;
				}
			}
		}
		return othersPos;
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
		if (!RegexValidate.isEmpty(pokers)) {
			StringBuffer sb = new StringBuffer();
			for (Poker poker:pokers) {
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
		if (!RegexValidate.isEmpty(pokers)) {
			StringBuffer sb = new StringBuffer();
			for (Poker poker:pokers) {
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
		if (pokers.isEmpty()) {
			return null;
		}
		int[] array = new int[pokers.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = pokers.get(i).getValue();
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
		if (pokers.isEmpty()) {
			return null;
		}
		String[] array = new String[pokers.size()];
		for (int i = 0; i < array.length; i++) {
			array[i] = pokers.get(i).getSymbol();
		}
		return array;
	}
	
	public static void main(String[] args) throws Exception {
		CowPoker cowPoker = new CowPoker("6-0,7-1,7-2,2-3,2-0");
		Map<String, String> level = cowPoker.level();
		Iterator<Entry<String, String>> it = level.entrySet().iterator();
		for(;it.hasNext();){
			Entry<String, String> next = it.next();
			System.out.println(next.getKey()+"--"+LEVELS.get(Integer.parseInt(next.getValue())));
		}
	}
}
