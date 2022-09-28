package com.xf.设计模式.建造者模式;

/**
 * 指导者类（Director）
 */
public class ComputerDirector {
    public void makeComputer(ComputerBuilder builder){
        builder.setUsbCount();
        builder.setDisplay();
        builder.setKeyboard();
    }
}