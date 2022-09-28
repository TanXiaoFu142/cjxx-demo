package com.xf.设计模式.单例模式;


/**
 * 单null检查
 * 使用这个写法的程序员应该说水平不是太高，这种写法应该被抛弃。
 * 其不是线程安全的，也就是说在多线程环境下，系统中有可能存在多个实例。
 * 除此之外，和上面一样通过反射也可以创建新的实例。
 */
public class Singleton2 {
    private static Singleton2 instance;

    private Singleton2() {
    }

    public static Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

}