package com.sanri.algorithm.poker;


/**
 * 
 * 创建时间:2016-5-8下午10:01:43<br/>
 * 创建者:sanri<br/>
 * 功能:javabean 一张扑克<br/>
 */
public class Poker implements Comparable<Poker> {
	/** 红桃 */
	public final static int HEARTS = 0;
	/** 黑桃 */
	public final static int SPADES = 1;
	/** 方块 */
	public final static int DIAMONDS = 2;
	/** 梅花 */
	public final static int CLUBS = 3;
	/** 王*/
	public final static int JOKER = 4;
	
	// 牌符号,从小到大依次为 A,1~10,J,Q,K,支持大小写
	private String symbol;
	// 值 ,从小到大依次为 1~13
	private int value;
	// 花色,常量值 ;Poker.HEARTS,Poker.SPADES,Poker.DIAMONDS,Poker.CLUBS
	private int suit;
	
	public Poker(){}
	public Poker(String symbol,int suit) throws NumberFormatException, Exception{
		this.symbol = symbol;
		this.value = Integer.parseInt(convert(symbol));
		this.suit = suit;
		if(suit < 0 || suit > 3){
			throw new Exception("不支持的花色");
		}
	}
	public Poker(int value,int suit) throws Exception{
		this.value = value;
		this.suit = suit;
		this.symbol = convert(String.valueOf(value));
		if(suit < 0 || suit > 3){
			throw new Exception("不支持的花色");
		}
	}
	/**
	 * 直接字符串构造一张牌
	 * 输入:牌符号-花色,不可使用数字字符 
	 * @param poker
	 * @throws Exception 
	 */
	public Poker(String poker) throws Exception{
		String[] split = poker.split("-");
		if(split.length != 2){
			throw new Exception("请输入牌符号和花色中间用 - 分隔");
		}
		this.symbol = split[0];
		this.value = Integer.parseInt(convert(symbol));
		this.suit= Integer.parseInt(split[1]);
		if(suit < 0 || suit > 3){
			throw new Exception("不支持的花色");
		}
	}
	
	// 转换符号和值
	public String convert(String symbol) throws Exception {
		if (symbol.matches("[2-9]") || "10".equals(symbol)) {
			return symbol;
		}
		if (symbol.matches("[aA]")) {
			return "1";
		}
		if (symbol.matches("[jJ]")) {
			return "11";
		}
		if (symbol.matches("[qQ]")) {
			return "12";
		}
		if (symbol.matches("[kK]")) {
			return "13";
		}
		int value = Integer.parseInt(symbol);
		switch (value) {
		case 1:
			return "A";
		case 11:
			return "J";
		case 12:
			return "Q";
		case 13:
			return "K";
		}
		throw new Exception("牌型转换异常 " + symbol);
	}

	@Override
	public int compareTo(Poker o) {
		int middle = this.value - o.value;
		if (middle == 0) {
			return this.suit - o.suit;
		}
		return middle;
	}

	@Override
	public String toString() {
//		return this.symbol + "-" + this.suit;
		String ret = String.valueOf(this.symbol);
		switch(suit){
		case 0:
			ret = "红桃" +ret;
			break;
		case 1:
			ret = "黑桃" +ret;
			break;
		case 2:
			ret = "方块" +ret;
			break;
		case 3:
			ret = "梅花" +ret;
			break;
		default:
			ret = "花色未找到,当前花色值为:"+this.suit+" 牌面值为:"+this.symbol;
		}
		return ret;
	}

	public boolean eq(Poker other) {
		return this.value == other.value && this.suit == other.suit;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() == Poker.class) {
			return eq((Poker) obj);
		}
		return super.equals(obj);
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) throws NumberFormatException, Exception {
		this.symbol = symbol;
		this.value = Integer.parseInt(convert(symbol));
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) throws Exception {
		this.value = value;
		this.symbol = convert(String.valueOf(value));
	}

	public int getSuit() {
		return suit;
	}

	public void setSuit(int suit) {
		this.suit = suit;
	}
}