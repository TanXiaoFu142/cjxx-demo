package com.xf.设计模式.观察者模式;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Observable;
import java.util.Observer;

/**
 * 观察者
 * 观察者模式（Observer Pattern）定义了对象间的一种一对多的依赖关系，这样只要一个对象的状态发生改变，其依赖的所有相关对象都会得到通知并自动更新。
 * 在观察者模式中，发生改变的对象叫做观察目标，而被通知更新的对象称为观察者，一个观察目标对应多个观察者，观察者一般是一个列表集合，可以根据需要动态增加和删除，易于扩展。
 * 使用观察者模式的优点在于观察目标和观察者之间是抽象松耦合关系，降低了两者之间的耦合关系。
 * 参考文章：https://mp.weixin.qq.com/s/bUUqp7DCVvQPP-H1EIGrnw
 * @author 谭俊杰
 * @date 2021/10/26
 * @time 16:41
 */
@RequiredArgsConstructor
public class ReaderObserver implements Observer {

    @NonNull
    private String name;

    private String article;

    @Override
    public void update(Observable o, Object arg) {
        updateArticle(o);
    }

    private void updateArticle(Observable o) {
        JavaStackObservable javaStackObservable = (JavaStackObservable) o;
        this.article = javaStackObservable.getArticle();
        System.out.printf("我是读者：%s，收到文章推送为：%s\n", this.name, this.article);
    }

    public static void main(String[] args) {
        // 创建一个观察目标
        JavaStackObservable javaStackObservable = new JavaStackObservable();

        // 添加观察者
        javaStackObservable.addObserver(new ReaderObserver("小明"));
        javaStackObservable.addObserver(new ReaderObserver("小张"));
        javaStackObservable.addObserver(new ReaderObserver("小爱"));

        // 发表文章
        javaStackObservable.publish("什么是观察者模式？");
    }
}
