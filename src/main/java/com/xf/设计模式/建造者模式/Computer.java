package com.xf.设计模式.建造者模式;

import lombok.Data;

/**
 * 建造者模式
 *      当一个类的构造函数参数个数超过4个，而且这些参数有些是可选的参数，考虑使用构造者模式。
 * 如何实现
 *   builder模式有4个角色。
 *      Product: 最终要生成的对象，例如 Computer实例。
 *      Builder： 构建者的抽象基类（有时会使用接口代替）。其定义了构建Product的抽象步骤，其实体类需要实现这些步骤。其会包含一个用来返回最终产品的方法Product getProduct()。
 *      ConcreteBuilder: Builder的实现类。
 *      Director: 决定如何构建最终产品的算法. 其会包含一个负责组装的方法 void Construct(Builder builder)， 在这个方法中通过调用builder的方法，就可以设置builder，等设置完成后，就可以通过builder的 getProduct() 方法获得最终的产品。
 * 参考文章：https://zhuanlan.zhihu.com/p/58093669
 * @author 谭俊杰
 * @date 2021/10/26
 * @time 13:42
 */
@Data
public class Computer {
    private String cpu;//必须
    private String ram;//必须
    private int usbCount;//可选
    private String keyboard;//可选
    private String display;//可选

    public Computer(String cpu, String ram) {
        this.cpu = cpu;
        this.ram = ram;
    }
}