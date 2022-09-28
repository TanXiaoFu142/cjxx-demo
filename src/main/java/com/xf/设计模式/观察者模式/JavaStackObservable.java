package com.xf.设计模式.观察者模式;

import lombok.Data;

import java.util.Observable;

/**
 * 观察目标对象
 * @author 谭俊杰
 * @date 2021/10/26
 * @time 16:38
 */
@Data
public class JavaStackObservable extends Observable {

    private String article;

    /**
     * 发表文章
     * @param article
     */
    public void publish(String article){
        // 发表文章
        this.article = article;

        // 改变状态
        this.setChanged();

        // 通知所有观察者
        /**
         * 先获取同步锁，判断状态是否更新，如已更新则清空观察目标状态，然后再使用 for 循环遍历所有观察者，一一调用观察者的更新方法通知观察者更新。
         */
        this.notifyObservers();
    }
}
