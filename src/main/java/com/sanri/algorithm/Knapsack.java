package com.sanri.algorithm;
class Fruit{
	private String name;
	private int size;
	private int price;
	
	public Fruit(String name,int size,int price){
		this.name=name;
		this.size=size;
		this.price=price;
	}
	public String getName(){
		return name;
	}
	public int getPrice(){
		return price;
	}
	public int getSize(){
		return size;
	}
}

public class Knapsack{
	public static void main(String[] args){
		final int MAX=8;
		final int MIN=1;
		int[] item=new int[MAX+1];
		int[] value=new int[MAX+1];
		Fruit fruits[]={
			new Fruit("李子",4,4500),
			new Fruit("苹果",5,5700),
			new Fruit("橘子",2,2250),
			new Fruit("草莓",1,1100),
			new Fruit("甜瓜",6,6700)
		};
		for(int i=0;i<fruits.length;i++){
			for(int s=fruits[i].getSize();s<=MAX;s++){//s表示现在背包的大小,依次将背包的空间设置为当前水果的尺寸累加
				int p=s-fruits[i].getSize();//表示每次增加单位背包空间，背包所剩的空间
				int newvalue=value[p]+fruits[i].getPrice();//value[p]表示增加的背包空间可以增加的价值，fruits[i].getprice()表示原有的背包的价值
				if(newvalue>value[s]){//现有的价值是否大于背包为s时的价值
					value[s]=newvalue;
					item[s]=i;//将当前的水果项添加到背包的物品中
				}
			}
		}
		System.out.println("物品\t价格");
		for(int i=MAX;i>MIN;i=i-fruits[item[i]].getSize()){
			System.out.println(fruits[item[i]].getName()+
				"\t"+fruits[item[i]].getPrice());
		}
		System.out.println("合计\t"+value[MAX]);
	}
}