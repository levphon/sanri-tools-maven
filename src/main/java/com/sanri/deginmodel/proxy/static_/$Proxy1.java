package com.sanri.deginmodel.proxy.static_; 
 public class $Proxy1 implements  Moveable {
    private Moveable target;

    public $Proxy1(Moveable target){
        this.target = target;
    }

    @Override
    public void move() {
        long startTime = System.currentTimeMillis();
        System.out.println("移动开始时间为:"+startTime);
        target.move();
        System.out.println("移动花费时间:"+(System.currentTimeMillis() - startTime));
    }
}
