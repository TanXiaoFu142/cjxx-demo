package com.xf.设计模式.单例模式;


/**
 * 静态常量模式
 * 参考文章：http://shusheng007.top/2021/09/08/015/
 */
public class Singleton1 {
    private final static Singleton1 INSTANCE = new Singleton1();

    private Singleton1() {
    }
    //这个简单粗暴，在类加载时候就创建了实例，属于**饿汉模式**。
    // 其是线程安全的，这一点由JVM来保证，但是有一个缺点，可以通过反射创建新的实例
    public static Singleton1 getInstance(){
        return INSTANCE;
    }
}