package com.xf.设计模式.建造者模式;

/**
 * @author 谭俊杰
 * @date 2021/10/28
 * @time 16:02
 */
public class TestJZZ {

    public static void main(String[] args) {
        ComputerDirector director = new ComputerDirector();
        ComputerBuilder builder = new MacComputerBuilder("I5处理器", "三星125");
        director.makeComputer(builder);
        Computer macComputer = builder.getComputer();
        System.out.println("mac computer:" + macComputer.toString());

        ComputerBuilder lenovoBuilder = new LenovoComputerBuilder("I7处理器", "海力士222");
        director.makeComputer(lenovoBuilder);
        Computer lenovoComputer = lenovoBuilder.getComputer();
        System.out.println("lenovo computer:" + lenovoComputer.toString());
    }
}
