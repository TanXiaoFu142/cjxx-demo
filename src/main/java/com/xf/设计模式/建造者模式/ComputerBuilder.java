package com.xf.设计模式.建造者模式;

/**
 * 抽象构建者类
 */
public abstract class ComputerBuilder {
    public abstract void setUsbCount();

    public abstract void setKeyboard();

    public abstract void setDisplay();

    public abstract Computer getComputer();
}