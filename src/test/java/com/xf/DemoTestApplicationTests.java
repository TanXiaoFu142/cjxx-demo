package com.xf;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.unit.DataUnit;
import cn.hutool.core.util.IdcardUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xf.entity.MasterEntity;
import com.xf.entity.PuPerformanceUnit;
import com.xf.service.PersonSync;
import com.xf.util.PinyinUtil;
import io.swagger.annotations.ApiModelProperty;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@SpringBootTest
class DemoTestApplicationTests {

    @Autowired
    private PersonSync personSync;
    @Test
    void contextLoads() {
        personSync.handlePerson();
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
        System.out.println("文字内容:" + chineseCar);
        System.out.println("拼音首字母：" + letter);
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
    void testRemove() {

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
    void testFormat() {
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
                "}";

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
    void testDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = DateUtil.parse("2023-01-01").toJdkDate();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        System.out.println( DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN));


        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println( DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN));
    }


    @Test
    void testThread() {
        System.out.println("Hello, World");

        // newFixedThreadPool(2) 中2 是表示创建一个只有两个线程的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // 执行任务
        executorService.execute(() -> {
            System.out.println("任务被执行,线程:" + Thread.currentThread().getName());
        });

    }

    @Test
    void testSplit(){
        List<String> signDetailList = new ArrayList<>(Arrays.asList("1","2","3","4","5","6","7","8","9"));
        List<String> replaceSignDetailList = new ArrayList<>(Arrays.asList("8","9","10"));

        //并集-->[1, 2, 3, 4, 5, 6, 7, 8, 9, 10] BankReceiptDocId
        System.out.println(CollectionUtil.union(signDetailList, replaceSignDetailList));

		/*//交集-->[8, 9]
        System.out.println(CollectionUtil.intersection(signDetailList, replaceSignDetailList));

        //补集-->[1, 2, 3, 4, 5, 6, 7, 10]
        System.out.println(CollectionUtil.disjunction(signDetailList, replaceSignDetailList));

        //补集-->[1, 2, 3, 4, 5, 6, 7, 10]
        System.out.println(CollectionUtil.disjunction(replaceSignDetailList,signDetailList));

        //差集-->[1, 2, 3, 4, 5, 6, 7]
        System.out.println(CollectionUtil.subtract(signDetailList, replaceSignDetailList));*/

        //差集-->[10]  请假的人上的班
        System.out.println(CollectionUtil.subtract(replaceSignDetailList,signDetailList));

        //利用差集[1, 2, 3, 4, 5, 6, 7, 8, 9]  本人上的班
        System.out.println(CollectionUtil.subtract(CollectionUtil.union(signDetailList, replaceSignDetailList),CollectionUtil.subtract(replaceSignDetailList,signDetailList)));

		//利用补集
        System.out.println(CollectionUtil.disjunction(CollectionUtil.union(signDetailList, replaceSignDetailList),CollectionUtil.subtract(replaceSignDetailList,signDetailList)));
        System.out.println(CollectionUtil.disjunction(CollectionUtil.subtract(replaceSignDetailList,signDetailList),CollectionUtil.union(signDetailList, replaceSignDetailList)));


        System.out.println(Arrays.asList(CollectionUtil.union(signDetailList, replaceSignDetailList)).toString().replace("[", "").replace("]", "").replaceAll(" ", ""));


    }


    @Test
    void testReflex(){
        PuPerformanceUnit puPerformanceUnit = new PuPerformanceUnit();
        Class<?> argObjClass = puPerformanceUnit.getClass();
        Field[] typeFields = ReflectUtil.getFields(argObjClass);
        for (Field field : typeFields) {
            if (field.isAnnotationPresent(ApiModelProperty.class)) {//判断是否存在@ApiModelProperty注解
                ApiModelProperty amp = field.getAnnotation(ApiModelProperty.class);
                System.out.println(amp.value());
            }
        }
    }


    @Test
    void testGroupBy(){

       List<MasterEntity> list = new ArrayList<>();
        MasterEntity entity1 = new MasterEntity();
        entity1.setName("张三");
        entity1.setState(3333);
        MasterEntity entity2 = new MasterEntity();
        entity2.setName("张三");
        entity2.setState(3333);
        MasterEntity entity3 = new MasterEntity();
        entity3.setName("张三");
        entity3.setState(3333);

        MasterEntity entity4 = new MasterEntity();
        entity3.setName("李四");
        entity3.setState(44444);

        list.add(entity1);
        list.add(entity2);
        list.add(entity3);

        Map<String, List<MasterEntity>> groupMap = list.stream().collect(Collectors.groupingBy(MasterEntity::getName));

        System.out.println(groupMap);
    }

    @Test
    void testDouble(){
        Double actualSalary = 0d;
        if (0==actualSalary) {
            System.out.println(true);
        }else{
            System.out.println(false);
        }
    }
}

