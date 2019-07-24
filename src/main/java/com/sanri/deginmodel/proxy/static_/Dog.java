package com.sanri.deginmodel.proxy.static_;

/**
 * 作者: sanri
 * 时间: 2017/08/10 21:25
 * 功能: 被代理的类
 */
public class Dog implements Moveable{

    @Override
    public void move() {
        System.out.println("狗移动中");
    }
}
