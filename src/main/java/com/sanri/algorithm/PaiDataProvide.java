package com.sanri.algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
/**
 * 
 * 创建时间:2016-10-2下午1:25:45<br/>
 * 创建者:sanri<br/>
 * 功能:翻牌算法,翻一张到底下然后出一张牌,使出牌顺序为 1 ~ 13<br/>
 */
public class PaiDataProvide {
	private int length;											//生成数据的长度
	private Map<Integer,String> keyValue;
	
	public static class GenType{
		public final static String  QUEUE = "queue";			//生成方式 以队列方式
		public final static String  STACK = "stack";			//生成方式 递归生成
	}
	
	private String genType = GenType.QUEUE;							//默认以队列方式生成
	
	public PaiDataProvide(String genType,int length){
		this.genType = genType;
		this.length = length;
	}
	
	/**
	 * 生成 len 长度的数据 数据从 1 开始 
	 * @param len
	 * @return
	 */
	private  int[] generateArr(){
		int [] arr = new int [length];
		for(int i=0;i<length;i++){
			arr[i] = i+1;
		}
		return arr;
	}
	
	/**
	 * 以递归方式排序
	 * @param arr
	 */
	private  void sort(int[] arr,List<String> list) {
		int len = arr.length;
		
		int [] inserted = new int [len/2];		//被插入的
		int [] insert = new int [len - len/2];	//插入
			
		//给插入和被插入赋值
		for(int i = 0;i < inserted.length;i++){
			inserted[i] = arr[i];
		}
		for(int i = inserted.length;i < arr.length;i++){
			insert[i-inserted.length] = arr[i];
		}
		
		if(len - len/2 !=1){
			sort(insert,list);
		}
		for(int i=0;i<list.size();i++){
			insert[i]= Integer.parseInt(list.get(i));
		}
		list.clear();
		if(insert.length == inserted.length){
			for(int j=0;j<insert.length;j++){
				list.add(String.valueOf(insert[j]));
				list.add(String.valueOf(inserted[j]));
			}
		}else{
			for(int j=0;j<inserted.length;j++){
				list.add(String.valueOf(insert[j+1]));
				list.add(String.valueOf(inserted[j]));
			}
			list.add(String.valueOf(insert[0]));
		}
	}
	
	
	
	private  List<Item> sort(){
		Queue<Item> queue = loadData();
		int index=1;
		int value=1;
		List<Item> list=new ArrayList<Item>();
		while (!queue.isEmpty()) {
			Item item =queue.peek();
			if(index++%2==0){
				item.setValue(String.valueOf(value++));
				list.add(item);
				queue.poll();
			}else{
				queue.poll();
				queue.offer(item);
			}
		}
		Collections.sort(list);
		return list;
	}
	
	private  Queue<Item> loadData() {
		Queue<Item> queue=new LinkedList<Item>();
		for (int i = 0; i < length; i++) {
			queue.offer(new Item(i+1, "0"));
		}
		return queue;
	}

	public static class Item implements Comparable<Item> {
		private int index;
		private String value;

		public Item(int index, String value) {
			super();
			this.index = index;
			this.value = value;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
		
		@Override
		public int compareTo(Item o) {
			return this.index - o.getIndex();
		}
	}

	
	public List<Item> getData(){
		List<Item> _list = new ArrayList<Item>();
		//两种方式来算出顺序
		if(GenType.QUEUE.equals(genType)){
			_list =  sort();
		}else{
			List<String> list = new ArrayList<String>();
			sort(generateArr(),list);
			for (int i = 0; i < list.size(); i++) {
				Item item = new Item(i+1, list.get(i));
				_list.add(item);
			}
		}
		//替换成字符而不是显示数字
		if(keyValue != null){
			for (Item item : _list) {
				String value = keyValue.get(Integer.parseInt(item.getValue()));
				if(value != null){
					item.setValue(value);
				}
			}
		}
		
		return _list;
	}

	public String getGenType() {
		return genType;
	}

	public void setGenType(String genType) {
		this.genType = genType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Map<Integer, String> getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(Map<Integer, String> keyValue) {
		this.keyValue = keyValue;
	}
}
