package com.xf;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.TimeoutUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@SpringBootTest
class DemoTestApplicationTests {

    @Test
    void contextLoads() {
    }

    /**
     * 整数反转
     * 输入123输出321
     * or
     * 输入1230输出321
     */
    @Test
    void reverse() {
        int x = 1230;
        System.out.println("输入：" + x);
        StringBuilder text = new StringBuilder();
        text.append(x);
        if (text.length() == 1) {
            System.out.println("输出：" + x);
        }
        String[] strings = text.toString().split("");
        text = new StringBuilder();
        for (int i = strings.length - 1; i >= 0; i--) {
            text.append(strings[i]);
        }
        System.out.println("反转后输出\n输出：" + Integer.valueOf(text.toString()));
    }

    @Test
    void testA() {

        Stream<Integer> stream2 = Stream.iterate(0, (x) -> x + 3).limit(4);
        stream2.forEach(System.out::println);

        Stream<Double> stream3 = Stream.generate(Math::random).limit(3);
        stream3.forEach(System.out::println);

    }

    @Test
    void testGetFirstLetter() {
        String chineseCar = "不知名建筑公司-aa";
        String letter = PinyinUtil.getAllFirstLetter(chineseCar);
        System.out.println("文字内容:"+chineseCar);
        System.out.println("拼音首字母："+letter);
    }

    @Test
    void testB() throws ParseException {
        String signMonth = "2022-09";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = format.parse(signMonth);
        System.out.println(date);
        Date beginDate = DateUtil.beginOfMonth(date);
        Date endDate = DateUtil.endOfMonth(date);
        System.out.println(beginDate);
        System.out.println(endDate);

        DateTime parse = DateUtil.parse(DateUtil.formatDate(endDate));
        System.out.println(parse);

    }

    @Test
    void testRemove(){

//        Iterator<Project> iterator = tendersList.iterator();
//        while (iterator.hasNext()) {
//            Project tenders = iterator.next();
//            if(tendersIds.contains(tenders.getId())){
//                iterator.remove();
//            }
//        }
        List<Integer> asList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        ArrayList<Integer> arrayList = new ArrayList<>(asList);
        ArrayList<Integer> integerArrayList = new ArrayList<>(asList);
        Iterator<Integer> iterator = arrayList.iterator();
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            if (integerArrayList.contains(next)) {
                iterator.remove();
            }
        }

        System.out.println("1");
        arrayList.stream().forEach(System.out::println);
        System.out.println("2");
    }

    void testGit(){

    }
}

