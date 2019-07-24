package com.sanri.algorithm.poker;

import java.util.Arrays;

import com.sanri.algorithm.poker.PokerList.AnalyseResult;

public class DoudizhuPoker {
	enum CardType{
		SINGLE,DOUBLE,THREE,BOMB,JOKER_BOMB,
		THREE_ONE,THREE_TWO,BOMB_TWO,BOMB_TWOOO,
		CONNECT,COMPANY,AIRCRAFT,AIRCRAFT_SINGLE,AIRCRAFT_DOUBLE,ERROR_CARD
	}
	
	public CardType input(Poker...pokers){
		Arrays.sort(pokers);
		switch(pokers.length){
		case 0:
			return CardType.ERROR_CARD;
		case 1:
			return CardType.SINGLE;
		case 2:
			if(pokers[0].getSuit() == Poker.JOKER && pokers[1].getSuit() == Poker.JOKER ){
				return CardType.JOKER_BOMB;
			}else if(pokers[0].getValue() == pokers[1].getValue()){
				return CardType.DOUBLE;
			}else{
				return CardType.ERROR_CARD;
			}
		}
		//三张以上的需分析扑克
		PokerList pokerList = new PokerList();
		for (Poker poker : pokers) {
			pokerList.add(poker);
		}
		AnalyseResult analyse = pokerList.analyse();
		//判断四张牌数量大于 0 的
		if(analyse.fourCount > 0){
			if(pokerList.size() == 4 && analyse.fourCount == 1){
				return CardType.BOMB;
			}
			//四带2
			if(pokerList.size() == 6 && analyse.fourCount == 1 && analyse.singleCount == 2 ){
				return CardType.BOMB_TWOOO;
			}
			//四带2对
			if(pokerList.size() == 8 && analyse.fourCount == 1 && analyse.doubleCount == 2){
				return CardType.BOMB_TWO;
			}
		}
		//三张牌的数量大于 0 的
		if(analyse.threeCount > 0){
			if(pokerList.size() == 3 && analyse.threeCount == 1){
				return CardType.THREE;
			}
			//两组及两组以上的只有可能是飞机
			if(analyse.threeCount > 1){
				//第一组第一张牌的值
				int firstPokerValue = analyse.threeCountData.get(0).getValue();
				//判断是不是连牌
				for(int i=1;i<analyse.threeCount;i++){
					int groupNextValue = analyse.threeCountData.get(i * 3).getValue();
					if(Math.abs(groupNextValue - firstPokerValue) != i){
						return CardType.ERROR_CARD;
					}
				}
			}
			if(analyse.threeCount * 3 == pokerList.size()){
				return CardType.AIRCRAFT;
			}
			if(analyse.threeCount * 4 == pokerList.size()){
				return CardType.AIRCRAFT_SINGLE;
			}
			if(analyse.threeCount * 5 == pokerList.size()){
				return CardType.AIRCRAFT_DOUBLE;
			}
			
			//特殊情况,人家把炸弹拆开,当飞机使,important 19 999888 这种 
			if(pokerList.size() == (analyse.fourCount + analyse.threeCount) * 4){
				return CardType.AIRCRAFT_SINGLE;
			}
		}
		//两张牌的数量大于3 的,一对前面已经说过了,只有可能是连对
		if(analyse.doubleCount >=3 && analyse.doubleCount * 2 == pokerList.size()){
			//第一组,第一张牌的值
			int firstPokerValue = analyse.doubleCountData.get(0).getValue();
			//判断是不是连牌
			for(int i=1;i<analyse.doubleCount;i++){
				int groupNextValue = analyse.doubleCountData.get(i * 2).getValue();
				if(Math.abs(groupNextValue - firstPokerValue) != i){
					return CardType.ERROR_CARD;
				}
			}
			return CardType.COMPANY;
		}
		//单张只有可能是连牌
		if(analyse.singleCount >= 5 && analyse.singleCount == pokerList.size()){
			//第一组,第一张牌的值
			int firstPokerValue = analyse.singleCountData.get(0).getValue();
			//判断是不是连牌
			for(int i=1;i<analyse.singleCount;i++){
				int groupNextValue = analyse.singleCountData.get(i).getValue();
				if(Math.abs(groupNextValue - firstPokerValue) != i){
					return CardType.ERROR_CARD;
				}
			}
			return CardType.CONNECT;
		}
		return CardType.ERROR_CARD;
	}
	
	public static void main(String[] args) throws Exception {
		Poker poker1 = new Poker(1,Poker.HEARTS);
		Poker poker2 = new Poker(9,Poker.HEARTS);
		Poker poker3 = new Poker(9,Poker.HEARTS);
		Poker poker4 = new Poker(9,Poker.HEARTS);
		Poker poker5 = new Poker(9,Poker.HEARTS);
		Poker poker6 = new Poker(8,Poker.HEARTS);
		Poker poker7 = new Poker(8,Poker.HEARTS);
		Poker poker8 = new Poker(8,Poker.HEARTS);
		CardType input = new DoudizhuPoker().input(poker2,poker2,poker3,poker4);
		System.out.println(input);
	}
}
