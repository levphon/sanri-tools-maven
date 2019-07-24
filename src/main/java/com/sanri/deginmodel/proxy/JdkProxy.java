package com.sanri.deginmodel.proxy;

import com.sanri.deginmodel.proxy.static_.Dog;
import com.sanri.deginmodel.proxy.static_.Moveable;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 作者: sanri
 * 时间: 2017/08/11 21:08
 * 功能: 使用 jdk 的动态代理
 */
public class JdkProxy {

    @Test
    public void testJdkProxy(){
        //创建目标对象
        Dog dog = new Dog();
        Moveable timeProxy = (Moveable)Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Moveable.class}, new TimeInvocationHandler(dog));
        timeProxy.move();
    }

    class TimeInvocationHandler implements InvocationHandler{
        private Moveable target;

        public TimeInvocationHandler(Moveable target){
            this.target = target;
        }
        /**
         *
         * @param proxy 代理类对象
         * @param method 执行的方法
         * @param args 方法参数
         * @return
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            long startTime = System.currentTimeMillis();
            System.out.println("移动开始时间为:"+startTime);
            method.invoke(target, args);
            System.out.println("移动花费时间:" + (System.currentTimeMillis() - startTime));
            return null;
        }
    }
}
