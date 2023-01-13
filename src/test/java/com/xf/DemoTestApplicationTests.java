package com.xf;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


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

    @Test
    void testGit() throws ParseException {
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Date parse = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(parse);
//        calendar.set(Calendar.HOUR_OF_DAY, 8);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.add(Calendar.DAY_OF_MONTH,-1);
//        //填报时间
//        Date time = calendar.getTime();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String format1 = simpleDateFormat.format(time);
//        System.out.println(format1);



        Calendar calen = Calendar.getInstance();
        calen.setTime(parse);
        calen.set(Calendar.HOUR_OF_DAY, 8);
        calen.set(Calendar.MINUTE, 0);
        calen.set(Calendar.SECOND, 0);
        String date = new SimpleDateFormat().format(calen.getTime());
        System.out.println(date);
    }

    @Test
    void testFormat(){
        String text = "{\n" +
                "    \"groupName\": \"上海申铁投资有限公司\",\n" +
                "    \"tendersId\": 11,\n" +
                "    \"personIdList\": [\n" +
                "        \"310108197208132813\"\n" +
                "    ],\n" +
                "    \"type\": \"GATE\",\n" +
                "    \"cameraId\": \"\",\n" +
                "    \"cameraName\": \"\",\n" +
                "    \"limitTime\": \"15\",\n" +
                "    \"projectId\": 1\n" +
                "}" ;

        JSONObject jsonObject = JSON.parseObject(text);
        JSONArray personIdList = jsonObject.getJSONArray("personIdList");
        System.out.println(personIdList);
    }

    @Test
    void formatString() throws ParseException {
//        String text = "2022-10-26T10:04:52.565+0000";
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//        Date parse = df.parse(text);
//        System.out.println(parse);
//        String format = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(parse);
//        System.out.println(format);
//
//
//        Date creationTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse("2022-11-17T08:32:05.638+0000");
//        String creationDate = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(creationTime);
//        System.out.println(creationDate);
//        Date date = new Date();
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        cal.add(Calendar.DAY_OF_MONTH,-1);
//        date = cal.getTime();
//        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
//        System.out.println(format);

//        String format = "HH:mm";
//        //范围开始时间
//        Date startTime = new SimpleDateFormat(format).parse("6:00");
//        //范围结束时间
//        Date endTime = new SimpleDateFormat(format).parse("22:00");
        //颗粒物浓度超过${potency}mg/m³，且次数>${count} 或者颗粒物浓度超过${potency1}mg/m³，且次数>${count1}


//        String pushContent = "${name}（${idNumber}）行程码${type}";
//        String name = "";
//        String idCard = "";
//        String remark ="";
//        String reason = PlaceholdersUtils.parse0(pushContent, new String[]{name, idCard,remark });
//        System.out.println(reason);
//        Date date = DateUtils.addHours(new Date(), -8);
//        String creationDate = new SimpleDateFormat("yyyy年MM月dd日  HH时mm分ss秒").format(date);
//        System.out.println(creationDate);

    }

    @Test
    void testDate(){
//        Calendar calendar = Calendar.getInstance();
//        calendar.add(Calendar.MONTH,-3);
//        String format = DateUtil.format(calendar.getTime(), "yyyy-MM");
//        System.out.println(format);
//        int i = DateUtil.dayOfMonth(new Date());
//        System.out.println(i);
        String [] str = {"1","2"};//["1","2"]
        System.out.println(str);
        List<String> strings = Arrays.asList(str);
        System.out.println(strings);
        int [] nums = {1,2};
        System.out.println(nums);
    }
    public static void main(String[] args) {
//
//        String signMonth = "2022-11";
//        Date date = DateUtil.parse(signMonth, "yyyy-MM").toJdkDate();
//        System.out.println(DateUtil.yesterday());
//        System.out.println(DateUtil.beginOfMonth(date).toString());
//
//
//        String text = "{\"gangjinlong\":\"20\",\"jiaozhu\":\"25\",\"chengcao\":\"10\",\"qingjiang\":\"15\"}";
//
//        JSONObject json = JSON.parseObject(text);
//        System.out.println("debug....");
//
//        //开始时间
//        Date startTime = DateUtil.beginOfMonth(DateUtil.parse("2022-05", "yyyy-MM")).toJdkDate();
//        //结束时间
//        Date endTime = DateUtil.endOfMonth(DateUtil.parse("2022-05", "yyyy-MM")).toJdkDate();
//
//        System.out.println(startTime);
//        System.out.println(endTime);
//
//        Integer a = 18;
//        Integer b = 31;
//
//        double signRate = 0L;
//        signRate = (double)a/b;
//        System.out.println(signRate);



//        String dateStr1 = "2017-03-01";
//        Date date1 = DateUtil.parse(dateStr1);
//
//        String dateStr2 = "2017-03-03";
//        Date date2 = DateUtil.parse(dateStr2);
//
//        //相差一个月，31天
//        long betweenDay = DateUtil.between(date1, date2, DateUnit.DAY);
//        System.out.println(betweenDay);

//        System.out.println(PinyinUtil.getFirstLetter("项目经理"));
//        System.out.println(PinyinUtil.getAllFirstLetter("项目经理"));

        Double beforeData = Double.valueOf("");
//        Double beforeData1111 = Double.valueOf(null);
        Double beforeData222 = Double.valueOf(0);

        System.out.println(beforeData);
//        System.out.println(beforeData1111);
        System.out.println(beforeData222);

        System.out.println("dev");
    }
}

