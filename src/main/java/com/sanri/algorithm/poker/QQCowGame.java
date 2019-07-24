package com.sanri.algorithm.poker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

public class QQCowGame {
	private int personNum;
	private boolean debug;
	
	//默认运行 10 次取平均值 
	private int repeatNum = 10;
	public QQCowGame(){}
	public QQCowGame(int personNum){
		this.personNum = personNum;
	}
	//所有的扑克牌
	private final static List<Poker> ALL_POKERS = new ArrayList<Poker>();
	static{
		//加载所有扑克牌
		for(int i=0;i<4;i++){
			try {
				for(int j=1;j<=13;j++){
					ALL_POKERS.add(new Poker(j, i));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public Map<String,String> averageProbability(Poker...pokers) throws Exception{
		if(debug){
			System.out.print("输入的所有牌为:");
			for (Poker poker : pokers) {
				System.out.print(poker+" ");
			}
			System.out.println();
		}
		Map<String,String> cowProbability = new LinkedHashMap<String, String>();
		if(pokers.length > 5){
			throw new Exception("牌数为误,请重新输入...");
		}
		if(pokers.length == 5){
			//可以计算组合了,返回也是组合
			List<Poker> pokerList = new ArrayList<Poker>();
			for (Poker poker : pokers) {
				pokerList.add(poker);
			}
			CowPoker cowPoker = new CowPoker(pokerList);
			return cowPoker.level();
		}
		if(personNum < 1){
			throw new Exception("请叫一些人来玩斗牛..");
		}else{
			if(debug){
				System.out.println("有 "+personNum+" 人在参与游戏");
			}
		}
		long [] levelSum = new long[16];
		if(debug){
			System.out.println("重复 "+repeatNum+" 计算");
		}
		for(int i=0;i<repeatNum;i++){
			long[] probability = probability(pokers);
			for (int j = 0; j < probability.length; j++) {
				levelSum[j] += probability[j];
			}
		}
		for(int i=0;i<levelSum.length - 1;i++){
			cowProbability.put(i+"", String.valueOf((float)levelSum[i]/levelSum[15]));
		}
		return cowProbability;
	}
	/**
	 * 
	 * 功能:求出每种出现的次数,和总次数<br/>
	 * 创建时间:2016-5-15下午10:29:56<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @param pokers
	 * @return
	 * @throws Exception<br/>
	 */
	private long [] probability(Poker...pokers) throws Exception{
		CopyOnWriteArrayList<Poker> copyAllPoker = new CopyOnWriteArrayList<Poker>(ALL_POKERS);
		List<Poker> removePokers = excludeOthers(copyAllPoker, pokers);
		CopyOnWriteArrayList<Poker> otherHavePokers = new CopyOnWriteArrayList<Poker>(removePokers);
		for (Poker poker : pokers) {
			otherHavePokers.remove(poker);
		}
		long allCombind = c(copyAllPoker.size(),5-pokers.length);
		//0-14 分别是无牛-牛 10 ,四花,五花,五小牛,四炸;15 位代表总数量; 长度为 16
		long [] levelSum =  new long [16];//{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,allCombind};
		levelSum[15] = allCombind;
		if(pokers.length == 4){
			//如果已经有四张牌,计算概率
			poker4Probability(pokers,otherHavePokers,copyAllPoker,levelSum);
			return levelSum;
		}
		return levelSum;
	}
	
	/**
	 * 
	 * 功能:如果手里面有四张牌,计算各组合的概率<br/>
	 * 创建时间:2016-5-15上午8:07:49<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @param pokers
	 * @param otherHavePokers
	 * @param levelSum
	 * @return<br/>
	 * @throws Exception 
	 */
	private void poker4Probability(Poker[] pokers, CopyOnWriteArrayList<Poker> otherHavePokers,List<Poker> copyAllPoker, long[] levelSum) throws Exception {
		List<Poker> pokers5 = new ArrayList<Poker>();
		CowPoker cowPoker = null;
		for (Poker poker : copyAllPoker) {
			pokers5.clear();
			//先把自己的牌加上
			for (Poker self : pokers) {
				pokers5.add(self);
			}
			//然后再加一张其它的牌
			pokers5.add(poker);
			//可以计算概率了
			cowPoker = new CowPoker(pokers5);
			Map<String, String> currentLevel = cowPoker.level();
			Iterator<Entry<String, String>> it = currentLevel.entrySet().iterator();
			for(;it.hasNext();){
				Entry<String, String> next = it.next();
				int value = Integer.parseInt(next.getValue());
				levelSum[value] ++;
				break;
			}	
		}
	}
	
	/**
	 * 
	 * 功能:随机排除别人的牌<br/>
	 * 创建时间:2016-5-14下午4:01:54<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @param copyAllPoker
	 * @param pokers
	 * @return
	 * @throws Exception<br/>
	 */
	private List<Poker> excludeOthers(CopyOnWriteArrayList<Poker> copyAllPoker, Poker... pokers) throws Exception {
		//排除掉别人的牌和自己的牌 (看别人的牌需要排除多少张)
		int excludePokerNum = pokers.length * (personNum - 1);
		List<Poker> removePokers = new ArrayList<Poker>();
		for (Poker poker : pokers) {
			if(copyAllPoker.remove(poker)){
				removePokers.add(poker);
			}else{
				throw new Exception("找不到需要移除的牌,这种情况一般不会发生,如果发生,1,2版本都出错了");
			}
		}
		//随机移除别人的牌(首先需把自己的牌排除在外,否则有可能移到自己的牌,这个在 cowGame2 有算错)
		for (int i =0 ;i<excludePokerNum;i++) {
			int index = (int) (Math.random() * (copyAllPoker.size() - 1)) + 1;
			Poker remove = copyAllPoker.remove(index);
			removePokers.add(remove);
		}
		if(debug){
			System.out.println("包含我自己的所有移除的 扑克:");
			System.out.println(removePokers);
		}
		return removePokers;
	}
	
	/**
	 * 
	 * 功能:c 组合,从 base 中选出 select 个数据<br/>
	 * 创建时间:2016-5-14下午3:07:56<br/>
	 * 作者：sanri<br/>
	 * 入参说明:<br/>
	 * 出参说明：<br/>
	 * @param base
	 * @param select
	 * @return<br/>
	 * @throws Exception 
	 */
	private static long c(int base,int select) throws Exception{
		if(select > base){
			throw new  Exception("不可能从 "+base+" 中选出 "+select + " 张牌");
		}
		long num = 1,num2 = 1;
		for(int k = base,i=select;k>0 && i > 0;k--,i--){
			num = num * k;
			num2 = num2 * i;
		}
		return num / num2;
	}
	
	public int getPersonNum() {
		return personNum;
	}
	public void setPersonNum(int personNum) {
		this.personNum = personNum;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	
//	public static void main(String[] args) throws NumberFormatException, Exception {
//		QQCowGame qqCowGame = new QQCowGame(3);
//		qqCowGame.setDebug(true);
		
//		
//		Map<String, String> probability = qqCowGame.averageProbability(poker1,poker2,poker3,poker4);
//		Iterator<Entry<String, String>> it = probability.entrySet().iterator();
//		float max = 0f;String maxLevel = "";
//		for(;it.hasNext();){
//			Entry<String, String> next = it.next();
//			String levelChinese = CowPoker.LEVELS.get(Integer.parseInt(next.getKey()));
//			String value = next.getValue();
//			System.out.println(levelChinese+"--"+value);
//			if(Float.parseFloat(value) >= max){
//				max = Float.parseFloat(value);
//				maxLevel = levelChinese;
//			}
//		}	
//		System.err.println("您最有可能得到:"+maxLevel+" 概率为:"+max);
//	}
	public static void main(String[] args) throws NumberFormatException, Exception {
		QQCowGame qqCowGame = new QQCowGame(3);
		boolean choice = true;
		Poker poker1 = new Poker("7", Poker.DIAMONDS);
		Poker poker2 = new Poker("2", Poker.CLUBS);
		Poker poker3 = new Poker("6", Poker.HEARTS);
		Poker poker4 = new Poker("3", Poker.SPADES);
		qqCowGame.setPersonNum(6);
		while(choice){
			BufferedReader br = null;
			br = new BufferedReader(new InputStreamReader(System.in));
//			System.out.println("几个人玩:");
//			String numPerson = br.readLine();
//			qqCowGame.setDebug(true);
			try {
				System.out.println("顺序输入4张牌,10 用 m 代替:");
				String readLine = br.readLine();
//				String[] split = readLine.split(",");
//				for (String pokerStr : split) {
//					String[] split2 = pokerStr.split("-");
//						Poker poker = new Poker(Integer.parseInt(split2[0]),Integer.parseInt(split2[1]));
//						pokers.add(poker);
//				}
//				poker1 = pokers.get(0);
//				poker2 = pokers.get(1);
//				poker3 = pokers.get(2);
//				poker4 = pokers.get(3);
				readLine = readLine.replaceAll("1", "A");
				String[] split = readLine.split("");
				for (int i=0;i<split.length;i++) {
					split[i] = split[i].replaceAll("m", "10");
				}
				poker1.setSymbol(split[1]);
				poker2.setSymbol(split[2]);
				poker3.setSymbol(split[3]);
				poker4.setSymbol(split[4]);
				Map<String, String> probability = null;
				if(split.length == 6){
					probability = qqCowGame.averageProbability(poker1,poker2,poker3,poker4,new Poker(split[5], Poker.HEARTS));
					Iterator<Entry<String, String>> it = probability.entrySet().iterator();
					for(;it.hasNext();){
						Entry<String, String> next = it.next();
						System.out.println(next.getKey()+"--"+CowPoker.LEVELS.get(Integer.parseInt(next.getValue())));
					}
					continue;
				}
				probability = qqCowGame.averageProbability(poker1,poker2,poker3,poker4);
				Iterator<Entry<String, String>> it = probability.entrySet().iterator();
				float max = 0f;String maxLevel = "";
				for(;it.hasNext();){
					Entry<String, String> next = it.next();
					String levelChinese = CowPoker.LEVELS.get(Integer.parseInt(next.getKey()));
					String value = next.getValue();
					System.out.println(levelChinese+"--"+value);
					if(Float.parseFloat(value) >= max){
						max = Float.parseFloat(value);
						maxLevel = levelChinese;
					}
				}	
				System.out.println("您最有可能得到:"+maxLevel+" 概率为:"+max);
			} catch (Exception e) {
				br.close();
				e.printStackTrace();
				break;
			}
		}
	}
}
