package com.xf.设计模式.单例模式;


/**
 * 静态内部类
 * 既是线程安全的，也是懒汉式的，那个实例只有在你首次访问时候才会生成。
 * 我们完全可以使用这种方式替换double-check方式。
 */
public class Singleton5 {
    private Singleton5() {
    }

    private static class SingletonInstance {
        private final static Singleton5 INSTANCE = new Singleton5();
    }

    public static Singleton5 getInstance() {
        return SingletonInstance.INSTANCE;
    }
}