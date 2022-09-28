package com.xf.设计模式.原型模式;

import java.util.ArrayList;
import java.util.List;

/**
 * 原型类
 */
public class Report implements Prototype {
    private List<String> parts;

    public Report() {
        this.parts = new ArrayList<>();
    }

    public Report(List<String> parts) {
        this.parts = parts;
    }
    //耗时的数据加载操作
    public void loadData() {
        parts.clear();
        parts.add("老夫聊发少年狂，左牵黄，右擎苍，锦帽貂裘，千骑卷平冈。");
        parts.add("为报倾城随太守，亲射虎，看孙郎。");
        parts.add("酒酣胸胆尚开张，鬓微霜，又何妨！持节云中，何日遣冯唐？");
        parts.add("会挽雕弓如满月，西北望，射天狼。");
    }

    public List<String> getContents() {
        return parts;
    }

    @Override
    public Prototype copy() {
        List<String> cloneList = new ArrayList<>(parts);
        return new Report(cloneList);
    }
}