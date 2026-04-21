package com.xf;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.*;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.AtomicDouble;
import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.Expression;
import com.stec.utils.*;
import com.xf.entity.*;
import com.xf.service.PersonSync;
import com.xf.util.HttpUtils;
import com.xf.util.PinyinUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.mutable.MutableDouble;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@EnableScheduling
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
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN));


        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        System.out.println(DateUtil.format(calendar.getTime(), DatePattern.NORM_DATETIME_PATTERN));
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
    void testSplit() {
        List<String> signDetailList = new ArrayList<>(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9"));
        List<String> replaceSignDetailList = new ArrayList<>(Arrays.asList("8", "9", "10"));

        System.out.println(CollectionUtil.disjunction(signDetailList, replaceSignDetailList));

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
        System.out.println(CollectionUtil.subtract(replaceSignDetailList, signDetailList));

        //利用差集[1, 2, 3, 4, 5, 6, 7, 8, 9]  本人上的班
        System.out.println(CollectionUtil.subtract(CollectionUtil.union(signDetailList, replaceSignDetailList), CollectionUtil.subtract(replaceSignDetailList, signDetailList)));

        //利用补集
        System.out.println(CollectionUtil.disjunction(CollectionUtil.union(signDetailList, replaceSignDetailList), CollectionUtil.subtract(replaceSignDetailList, signDetailList)));
        System.out.println(CollectionUtil.disjunction(CollectionUtil.subtract(replaceSignDetailList, signDetailList), CollectionUtil.union(signDetailList, replaceSignDetailList)));


        System.out.println(Arrays.asList(CollectionUtil.union(signDetailList, replaceSignDetailList)).toString().replace("[", "").replace("]", "").replaceAll(" ", ""));


    }

    @Test
    void testsa() throws ParseException {
        //本人出勤
        List<String> signDetailList = new ArrayList<>(Arrays.asList("1,3,5,7,9,10".split(",")));
        //代班人出勤
        List<String> replaceSignDetailList = new ArrayList<>(Arrays.asList("9".split(",")));
        //请假出勤
        Set<String> replaceSignDetailListAll = new HashSet<>(Arrays.asList("9,10".split(",")));

        //获取本人出勤和请假出勤的差集
        Collection<String> subtract = CollectionUtil.subtract(signDetailList, replaceSignDetailList);
        System.out.println(subtract);

        //
//        System.out.println("本人出勤："+detail);
//        System.out.println("代办出勤："+replace);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        List<DateTime> dateTimes = DateUtil.rangeToList(DateUtil.parseDateTime("2024-01-11 00:00:00"), DateUtil.parseDateTime("2024-01-11 00:00:00"), DateField.DAY_OF_YEAR);
//        for (DateTime dateTime : dateTimes) {
//            System.out.println(DateUtil.dayOfMonth(dateTime));
//        }
    }

    @Test
    void testReflex() {
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
    void testGroupBy() {

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

        Map<String, List<MasterEntity>> groupMap = list.stream()
                .collect(Collectors.groupingBy(MasterEntity::getName));

        System.out.println(groupMap);
    }

    @Test
    void testDouble() {
        Double actualSalary = 0d;
        if (0 == actualSalary) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }


    @Test
    void testJSONObject() {
        String text = "{\n" +
                "  \"track_phase_1\": {\n" +
                "    \"xmjdwcqkList\": [\n" +
                "      {\n" +
                "        \"gzjd\": \"工作节点\",\n" +
                "        \"wcsx\": \"完成时限\",\n" +
                "        \"wcqk\": \"完成情况\",\n" +
                "        \"id\": \"0-34269\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"bytzwcqkList\": [\n" +
                "      {\n" +
                "        \"xmmc\": \"项目名称\",\n" +
                "        \"gstze\": \"估算投资额\",\n" +
                "        \"byywc\": \"已完成\",\n" +
                "        \"byzydjhb\": \"占月度计划比\",\n" +
                "        \"bnywc\": \"已完成\",\n" +
                "        \"bnzndjhb\": \"占年度计划比\",\n" +
                "        \"klywc\": \"已完成\",\n" +
                "        \"klzgsjhb\": \"占概算计划比\",\n" +
                "        \"id\": \"0-16446\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"ndjhList\": [\n" +
                "      {\n" +
                "        \"yf\": \"2023-01\",\n" +
                "        \"tjfy\": \"有轨一期土建费用1\",\n" +
                "        \"zcfy\": \"有轨一期征迁费用\",\n" +
                "        \"qtfy\": \"有轨一期其他费用\",\n" +
                "        \"hj\": \"有轨一期合计\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-02\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-03\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-04\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-05\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-06\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-07\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-08\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-09\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-10\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-11\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-12\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"czwtList\": [\n" +
                "      {\n" +
                "        \"nr\": \"问题内容\",\n" +
                "        \"id\": \"0-51616\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"xybjhList\": [\n" +
                "      {\n" +
                "        \"nr\": \"计划内容\",\n" +
                "        \"id\": \"0-55133\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"comName\": \"prophaseCom\"\n" +
                "  },\n" +
                "  \"fengnan_line\": {\n" +
                "    \"zyxxjdzb\": [],\n" +
                "    \"bytzwcqkList\": [],\n" +
                "    \"ndjhList\": [\n" +
                "      {\n" +
                "        \"yf\": \"2023-01\",\n" +
                "        \"tjfy\": \"枫南线土建费用\",\n" +
                "        \"zcfy\": \"枫南线征迁费用\",\n" +
                "        \"qtfy\": \"枫南线其他费用\",\n" +
                "        \"hj\": \"枫南线合计\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-02\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-03\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-04\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-05\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-06\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-07\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-08\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-09\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-10\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-11\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-12\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"czwtList\": [],\n" +
                "    \"xybjhList\": [],\n" +
                "    \"comName\": \"ingCom\"\n" +
                "  },\n" +
                "  \"xitang_line\": {\n" +
                "    \"zyxxjdzb\": [],\n" +
                "    \"bytzwcqkList\": [],\n" +
                "    \"ndjhList\": [\n" +
                "      {\n" +
                "        \"yf\": \"2023-01\",\n" +
                "        \"tjfy\": \"西塘线土建费用\",\n" +
                "        \"zcfy\": \"西塘线征迁费用\",\n" +
                "        \"qtfy\": \"西塘线其他费用\",\n" +
                "        \"hj\": \"西塘线合计\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-02\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-03\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-04\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-05\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-06\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-07\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-08\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-09\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-10\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-11\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-12\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"czwtList\": [],\n" +
                "    \"xybjhList\": [],\n" +
                "    \"comName\": \"ingCom\"\n" +
                "  },\n" +
                "  \"test-1\": {\n" +
                "    \"xmjdwcqkList\": [],\n" +
                "    \"bytzwcqkList\": [],\n" +
                "    \"ndjhList\": [\n" +
                "      {\n" +
                "        \"yf\": \"2023-01\",\n" +
                "        \"tjfy\": \"收尾项目1土建费用\",\n" +
                "        \"zcfy\": \"收尾项目1征迁费用\",\n" +
                "        \"qtfy\": \"收尾项目1其他费用\",\n" +
                "        \"hj\": \"收尾项目1合计\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-02\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-03\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-04\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-05\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-06\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-07\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-08\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-09\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-10\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-11\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"yf\": \"2023-12\"\n" +
                "      }\n" +
                "    ],\n" +
                "    \"czwtList\": [],\n" +
                "    \"xybjhList\": [],\n" +
                "    \"comName\": \"endingCom\"\n" +
                "  },\n" +
                "  \"test-other1\": {\n" +
                "    \"qtxmwcqk\": \"\\u003cp\\u003e其他项目完成情况\\u003c/p\\u003e\",\n" +
                "    \"comName\": \"otherCom\"\n" +
                "  }\n" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(text);
        JSONObject track_phase_1 = jsonObject.getJSONObject("track_phase_1");
        String xmjdwcqkList = track_phase_1.getString("xmjdwcqkList");
        System.out.println(xmjdwcqkList);
    }


    @Test
    void testLocalDateTime() throws ParseException {
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time);//2023-11-24T18:03:37.819

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String format = time.format(fmt);
        System.out.println(format);

        SimpleDateFormat sdf = new SimpleDateFormat(DatePattern.NORM_DATETIME_PATTERN);

        Date date = sdf.parse(format);
        // 将 Date 转换为 Instant
        Instant instant = date.toInstant();
        // 将 Instant 转换为 LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        System.out.println(localDateTime);

    }

    @Test
    void testDateUtil_Between() {

        Date startDate = DateUtil.parse("2023-12-08 14:52:29", DatePattern.NORM_DATETIME_PATTERN).toJdkDate();
        Date endDate = DateUtil.parse("2023-12-08 15:52:29", DatePattern.NORM_DATETIME_PATTERN).toJdkDate();

        long day = DateUtil.between(endDate, startDate, DateUnit.DAY);
        System.out.println(day);

    }

    @Test
    public void data1() {
//        String a = "2024-03-01 00:00:00";
//        String b = "2024-03-12 00:00:00";
//        DateTime dateTimea = DateUtil.parseDateTime(a);
//        DateTime dateTimeb = DateUtil.parseDateTime(b);
//        List<DateTime> dayList = DateUtil.rangeToList(dateTimea, dateTimeb, DateField.DAY_OF_YEAR);//创建日期范围生成器
//        System.out.println(dayList);
//        List<DateTime> weekList = DateUtil.rangeToList(dateTimea, dateTimeb, DateField.WEEK_OF_YEAR);//创建日期范围生成器
//        System.out.println(weekList);


        // 定义开始日期和结束日期
        LocalDate startDate = LocalDate.of(2024, 3, 1);
        LocalDate endDate = LocalDate.of(2024, 3, 12);

        // 计算两个日期之间的天数差
        long days = ChronoUnit.DAYS.between(startDate, endDate);

        // 获取每个日期的周信息
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i <= days; i++) {
            LocalDate date = startDate.plusDays(i);
            dates.add(date);
        }

        // 获取每个日期的周信息
        List<Integer> weeks = new ArrayList<>();
        for (LocalDate date : dates) {
            int week = date.get(WeekFields.of(DayOfWeek.MONDAY, 1).weekOfYear());
            weeks.add(week);
        }

        // 输出结果
        for (int i = 0; i < dates.size(); i++) {
            LocalDate date = dates.get(i);
            int week = weeks.get(i);
            System.out.println("Date: " + date + ", Week: " + week);
        }
    }

    @Test
    public void date2() {
        // 获取当前日期
        String currentDate = "2024-12-30"/*DateUtil.today()*/;
        System.out.println("当前日期：" + currentDate);

        DateTime date = DateUtil.parse(currentDate);
        // 计算当前日期是一年中的第几周
        int currentWeekOfYear = DateUtil.weekOfYear(date);
        System.out.println("当前周数：" + currentWeekOfYear);

        //当前时间所在周开始时间
        Date beginOfMonth = DateUtil.beginOfWeek(date);
        //当前时间所在周结束时间
        Date endOfMonth = DateUtil.endOfWeek(date);

        // 本周开始时间
        System.out.println("本周开始时间：" + DateUtil.formatDate(beginOfMonth));
        // 本周结束时间
        System.out.println("本周结束时间：" + DateUtil.formatDate(endOfMonth));


    }

    @Test
    public void date3() {
        Set<String> detailsSet = new HashSet<>();
        LocalDate localDate = LocalDate.of(Integer.valueOf("2024"), Integer.valueOf("3"), Integer.valueOf("13"));
        detailsSet.add(localDate.toString());
        System.out.println(detailsSet);
    }

    @Test
    void testRequest() throws IOException {
        JSONObject dataBoby = new JSONObject();
        dataBoby.put("username", "zhgd_50");
        dataBoby.put("password", "Zh_0901#");

        String resultToken = Jsoup.connect("https://jgpt.shsttz.com/receive-http/login")
                .header("Content-Type", "application/json")
                .ignoreContentType(true)
                .requestBody(dataBoby.toJSONString())
                .post().text();
        String token = JSONObject.parseObject(resultToken).getString("result");
        System.out.println(token);
//         String data = "{\"3006\":76.0,\"3005\":2.0,\"3004\":85.6,\"3003\":3.3,\"3002\":72.3,\"3001\":0.033,\"psn\":\"20231215104206005\",\"time\":1702864440000}";
        String data = "{\n" +
                "  \"data\":[\n" +
                "    {\n" +
                "      \"psn\":20231215104206005,\n" +
                "      \"time\":1702864421175,\n" +
                "      \"data\":{\n" +
                "        \"3001\":1, \n" +
                "        \"3002\":1,\n" +
                "        \"3003\":1, \n" +
                "        \"3004\":1, \n" +
                "        \"3005\":1, \n" +
                "        \"3006\":1 \n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        String result = Jsoup.connect("https://jgpt.shsttz.com/receive-http/stecSend")
                .header("x-auth-token", token)
                .header("Content-Type", "application/json;charset=UTF-8")
                .ignoreContentType(true)
                .requestBody(data)
                .post().text();
        System.out.println(System.currentTimeMillis());
        System.out.println(result);
    }

    @Test
    void testSort() {
//        //危险>风险预警>风险提示>危险可控>安全


//        /**
//         * 安全  SAFETY
//         * 风险可控 CONTROLLABLE
//         * 危险 DANGEROUS
//         * 风险预警 RISK_WARNING
//         * 风险提示 RISK_HINT
//         */
//        Comparator<String> comparator = new Comparator<String>() {
//            int getLevel(String s) {
//                if (s.equals("DANGEROUS")) {
//                    return 0;
//                }
//                if (s.equals("RISK_WARNING")) {
//                    return 1;
//                }
//                if (s.equals("RISK_HINT")) {
//                    return 2;
//                }
//                if (s.equals("CONTROLLABLE")) {
//                    return 3;
//                }
//                if (s.equals("SAFETY")) {
//                    return 4;
//                }
//                return 5;
//            }
//
//            @Override
//            public int compare(String o1, String o2) {
//                int level1 = getLevel(o1);
//                int level2 = getLevel(o2);
//                return Integer.compare(level1, level2);
//            }
//        };
//        String shd = "CONTROLLABLE";
//        String monitor = "RISK_WARNING";
//        String assessSafety = comparator.compare(shd, monitor) < 0 ? shd : monitor;
//        System.out.println(assessSafety);

        //排序规则
        HashMap<Integer, String> sortMap = new HashMap<>();
        sortMap.put(4, "0001.0002");
        sortMap.put(1, "0002.0001");
        sortMap.put(5, "0001.0002.0001");
        sortMap.put(3, "0002.0002");
        sortMap.put(7, "0003");
//把这些数据添加到HashMap<Integer, String> sortMap = new HashMap<>();中

        /**
         *  1	00001
         * 11	00001.00004
         * 12	00001.00005
         * 13	00001.00006
         * 14	00001.00007
         * 17	00003
         * 18	00001.00008
         * 19	00001.00009
         * 20	00001.00010
         * 21	00001.00011
         * 22	00001.00012
         * 23	00001.00013
         * 24	00001.00014
         * 25	00001.00015
         * 26	00001.00016
         * 27	00001.00017
         * 28	00001.00018
         * 29	00001.00019
         * 30	00004
         * 31	00001.00020
         * 32	00001.00021
         * 33	00001.00022
         * 34	00001.00023
         * 35	00001.00024
         * 37	00004.00002
         * 38	00003.00021
         * 40	00003.00002
         * 41	00003.00024
         * 42	00003.00004
         * 43	00003.00005
         * 44	00003.00006
         * 45	00003.00007
         * 46	00003.00008
         * 47	00003.00009
         * 48	00003.00010
         * 49	00003.00011
         * 50	00003.00012
         * 51	00003.00013
         * 52	00003.00014
         * 53	00003.00015
         * 55	00003.00017
         * 56	00003.00018
         * 57	00003.00019
         * 58	00003.00020
         * 60	00003.00022
         * 61	00003.00023
         * 62	00004.00003
         * 63	00005
         * 64	00003.00025
         * 65	00003.00026
         * 66	00003.00027
         * 67	00003.00028
         * 68	00003.00029
         * 69	00003.00030
         * 70	00005.00001
         * 71	00006
         * 72	00007
         * 73	00008
         * 74	00009
         * 79	00003.00031
         * 80	00007.00001
         * 83	00003.00032
         * 85	00008.00001
         * 87	00005.00005
         * 88	00005.00010
         * 89	00005.00008
         * 90	00005.00012
         * 91	00005.00019
         * 92	00001.00025
         * 93	00001.00026
         * 94	00005.00002
         * 95	00005.00003
         * 96	00005.00004
         * 97	00005.00006
         * 98	00005.00007
         * 99	00005.00009
         * 100	00005.00011
         * 101	00004.00004
         * 103	00005.00020
         * 104	00001.00027
         * 105	00005.00021
         * 106	00003.00033
         * 107	00003.00034
         * 108	00003.00035
         * 109	00006.00001
         * 110	00003.00036
         * 111	00003.00037
         * 112	00003.00038
         * 113	00003.00039
         * 115	00003.00040
         * 116	00003.00041
         * 118	00001.00028
         * 119	00001.00029
         * 120	00008.00002
         * 121	00008.00003
         * 122	00008.00004
         * 123	00008.00005
         * 124	00008.00006
         * 125	00008.00007
         * 126	00008.00008
         * 127	00008.00009
         * 128	00008.00010
         * 129	00008.00011
         * 130	00008.00012
         * 131	00008.00013
         * 132	00008.00014
         * 133	00008.00015
         * 134	00008.00016
         * 135	00003.00042
         * 136	00001.00030
         * 138	00001.00031
         * 139	00007.00002
         * 140	00004.00005
         * 141	00006.00002
         * 142	00005.00023
         * 144	00010
         * 145	00011
         * 146	00012
         * 147	00001.00032
         * 148	00013
         * 150	00015
         * 154	00016
         * 155	00017
         * 156	00001.00033
         * 157	00018
         * 158	00001.00034
         * 159	00001.00035
         * 160	00009.00002
         * 161	00009.00003
         * 164	00019
         * 166	00001.00036
         */
        //需要排序的数据
        List<MasterEntity> list = new ArrayList<>();
        list.add(new MasterEntity<>("第三", 1));
        list.add(new MasterEntity<>("第四", 3));

        list.add(new MasterEntity<>("第一", 4));
        list.add(new MasterEntity<>("第二", 5));
        list.add(new MasterEntity<>("第五", 7));


        //list中的数据的state属性和根据sortMap中的key对应，根据sortMap中的value进行排序
        list.sort(Comparator.comparing(o -> sortMap.get(o.getState())));
        for (MasterEntity masterEntity : list) {
            System.out.println(masterEntity.getName() + " " + masterEntity.getState());
        }
    }

    @Test
    void testOffsetDay() {

//        String dateStr = "2023-02-28 00:00:00";
//        Date date = DateUtil.parse(dateStr);
//        DateTime newDate2 = DateUtil.offsetDay(date, 3);
        System.out.println(DateUtil.endOfDay(new Date()));

        System.out.println(DateUtil.beginOfDay(new Date()));
    }

    @Test
    void testSum() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(1, 23);
        map.put(2, 16);
        map.put(3, 30);
        map.put(4, 19);
        map.put(5, 25);
        map.put(6, 24);
        map.put(7, 28);
        map.put(8, 26);
        map.put(9, 21);
        map.put(10, 12);
        map.put(11, 24);
        map.put(12, 18);
        map.put(13, 33);
        map.put(14, 19);
        map.put(15, 19);
        map.put(16, 19);
        map.put(17, 19);
        map.put(18, 28);
        map.put(19, 17);
        map.put(20, 21);
        map.put(21, 16);
        map.put(22, 22);
        map.put(23, 16);
        map.put(24, 32);
        map.put(25, 20);
        map.put(26, 32);
        map.put(27, 16);
        map.put(28, 26);
        map.put(29, 18);
        map.put(30, 27);
        map.put(31, 18);
        map.put(32, 31);
        map.put(33, 14);
        map.put(34, 29);
        map.put(35, 22);

        for (int i = 1; i <= 35; i++) {
            System.out.print("i=" + i + "：");
            if (i == 32) {
                System.out.println(map.get(i) + map.get(i + 1) + map.get(i + 2) + map.get(i + 3));
                break;
            } else {
                System.out.println(map.get(i) + map.get(i + 1) + map.get(i + 2) + map.get(i + 3) + map.get(i + 4));
            }

        }


    }

    @Test
    void testRangeToList() {


//        ArrayList<String> list = new ArrayList<>(Arrays.asList("2024-01-01", "2024-02-01", "2024-03-01", "2024-04-01", "2024-05-01", "2024-06-01", "2024-07-01", "2024-08-01", "2024-09-01", "2024-10-01", "2024-11-01", "2024-12-01"));
//        for (String str : list) {
//            DateTime dateTime = DateUtil.parse(str, DatePattern.NORM_DATE_PATTERN);
//            int monthDay = DateUtil.lengthOfMonth(DateUtil.month(dateTime)+1, DateUtil.isLeapYear(DateUtil.year(dateTime)));
//            System.out.println(str+":::"+monthDay);
//        }


        int aqyNumber = 10;
        int lwNumber = 20;
        double rate = (double) aqyNumber / lwNumber;
        boolean c = rate < 0.02;
        System.out.println(c);
    }


    public Map<Integer, String> getLongStringMap() {
        Map<Integer, String> idMap = new HashMap<>();
        idMap.put(11, "00001.00004");
        idMap.put(12, "00001.00005");
        idMap.put(13, "00001.00006");
        idMap.put(14, "00001.00007");
        idMap.put(18, "00001.00008");

        Map<Integer, String> sortedIdMap = new TreeMap<>(Comparator.comparing(idMap::get));
        sortedIdMap.putAll(idMap);
        sortedIdMap.forEach((k, v) -> System.out.println(k + ":" + v));
        return sortedIdMap;
    }

    public List<MasterEntity> getList() {
        List<MasterEntity> list = new ArrayList<>();
        list.add(new MasterEntity<>("第三", 14));
        list.add(new MasterEntity<>("第四", 18));
        list.add(new MasterEntity<>("第一", 11));
        list.add(new MasterEntity<>("第二", 13));
        list.add(new MasterEntity<>("第五", 18));
        return list;

    }

    @Test
    public void testComparingTreeId() {
        Map<Integer, String> idMap = this.getLongStringMap();
        List<MasterEntity> list = this.getList();
        //根据list里的对象的state在idMap中对应的value进行升序排序


        System.out.println("方式一：");
        list = list.stream().sorted(Comparator.comparing(obj -> idMap.get(obj.getState()))).collect(Collectors.toList());
        list.forEach(System.out::println);
        System.out.println("方式二：");
        list.sort(Comparator.comparing(obj -> idMap.get(obj.getState())));
        list.forEach(System.out::println);

    }

    @Test
    public void testFor() {
        ArrayList<Long> list = new ArrayList<>(Arrays.asList(1L, 2L, 3L, 4L, 3L, 2L, 1L, 3L, 4L));
        ArrayList<Long> listA = new ArrayList<>(Arrays.asList(11L));
    }


//    public static void main(String[] args){
//        MasterEntity<Serializable> a = new MasterEntity<>("张三", 1);
//        MasterEntity<Serializable> b = new MasterEntity<>("李四", 2);
//        System.out.println(a+":::"+a.hashCode());
//        System.out.println(b+":::"+b.hashCode());
//        //输出a的引用地址
//        List<MasterEntity<Serializable>> list = new ArrayList<>(Arrays.asList(a, b));
//        for (MasterEntity<Serializable> master : list) {
//            System.out.println(master+":::"+master.hashCode());
//        }
//        setList(list);
//        for (MasterEntity<Serializable> master : list) {
//            System.out.println(master+":::"+master.hashCode());
//        }
//
//    }
//
//    public static void setList(List<MasterEntity<Serializable>> list) {
//        for (MasterEntity<Serializable> master : list) {
//            master.setName("6666");
//        }
//    }
//
//    public static void main(String[] args){
//        Boolean flag = false; // 初始化 flag 为 false
//        setFlag(flag);       // 将 flag 的值传入 setFlag 方法
//        System.out.println(flag); // 输出 flag 的值，结果为 false
//    }
//
//    public static void setFlag(Boolean flag) {
//        flag = true; // 修改参数 flag 的值，但不会影响到 main 方法中的 flag
//    }

    public static final Integer flagKey = 10;

    public static void main(String[] args) {
//        List<MasterEntity> list = new ArrayList<>(
//                Arrays.asList(
//                        new MasterEntity("张三", 3),
//                        new MasterEntity("李四", 4),
//                        new MasterEntity("王五", 5),
//                        new MasterEntity("赵六", 6))
//        );
//        ExecutorService executorService = Executors.newFixedThreadPool(list.size());
//        List<CompletableFuture<Void>> taskList = new ArrayList<>();
//        for (MasterEntity entity : list) {
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                setMasterEntity(entity);
//            }, executorService);
//            taskList.add(future);
//        }
//        StopWatch stopWatch = new StopWatch();
//        stopWatch.start();
//        CompletableFuture.allOf(taskList.toArray(new CompletableFuture[0])).join();
//        executorService.shutdown();
//        list.forEach(System.out::println);
//        stopWatch.stop();
//        System.out.println(stopWatch.prettyPrint());

        Map<String, String> columnMap = new HashMap<String, String>() {{
            put("projectId__name", "所属项目");
            put("tenderId__name", "标段名称");
            put("problemField__name", "问题专业");
            put("problemLevel__name", "问题等级");
            put("problemDescription", "问题描述");
            put("applyUser", "提出人");
            put("applyDate", "提出时间");
            put("status", "消缺整改状态");
            put("approveUsers__name", "当前审核人");
        }};
        //columnMap转json
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(columnMap);
        System.out.println(jsonObject);
    }

    public static void setMasterEntity(MasterEntity entity) {
        try {
            System.out.println("进来" + entity.getName());
            Thread.sleep(1000 * 5);
            entity.setState(entity.getState() + flagKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testDateParse() {
        String signMonth = "2024-07";
        DateTime parse = DateUtil.parse(signMonth);
        System.out.println(DateUtil.formatDateTime(parse));
    }

    /**
     * 不创建对象的写
     */
    @Test
    public void noModelWrite() {
        // 写法1
        String fileName = "noModelWrite" + System.currentTimeMillis() + ".xlsx";
        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        Map<String, String> columnMap = new HashMap<String, String>() {{
            put("projectId__name", "所属项目");
            put("tenderId__name", "标段名称");
            put("problemField__name", "问题专业");
            put("problemLevel__name", "问题等级");
            put("problemDescription", "问题描述");
            put("applyUser", "提出人");
            put("applyDate", "提出时间");
            put("status", "消缺整改状态");
            put("approveUsers__name", "当前审核人");
        }};

        //获取动态头部
        List<List<String>> headList = this.getHeadList(columnMap);

        //获取动态数据源
        List<List<Object>> dataList = this.getDataList(columnMap);

        EasyExcel.write(fileName).head(headList).registerWriteHandler(new SheetWriteHandler() {
            @Override
            public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
                Sheet sheet = writeSheetHolder.getSheet();
                sheet.createFreezePane(2, 2, 0, 0);
            }
        }).sheet("模板").doWrite(dataList);
    }


    private List<List<String>> getHeadList(Map<String, String> columnMap) {
        List<List<String>> headList = ListUtils.newArrayList();
        if (MapUtils.isEmpty(columnMap)) {
            throw new RuntimeException("导出Excel表头未设置！");
        }
        for (String columnTitle : columnMap.values()) {
            List<String> list = ListUtils.newArrayList();
            list.add(columnTitle);
            headList.add(list);
        }


        return headList;
    }

    private List<List<Object>> getDataList(Map<String, String> columnMap) {
        List<List<Object>> resultList = ListUtils.newArrayList();

        //数据库查询数据
        List<JSONObject> jsonList = this.getJsonList();

        //获取columnMap中值为true的键
        Set<String> columnCodeSet = columnMap.keySet();
        //过滤数据
        for (JSONObject json : jsonList) {
            //行数据
            List<Object> data = ListUtils.newArrayList();
            for (String column : columnCodeSet) {
                data.add(json.get(column));
            }
            resultList.add(data);
        }
        return resultList;
    }


    /**
     * 模拟数据库查询数据
     *
     * @return
     */
    private List<JSONObject> getJsonList() {
        List<JSONObject> resultList = new ArrayList<>();
        //读取项目resource目录下jsonFile文件夹中的metadata.json文件 java 8
        File file = new File("src/main/resources/templates/jsonFile/metadata.json");
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray jsonArray = JSONArray.parseArray(content);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                resultList.add(JSONObject.parseObject(jsonObject.getString("metadata")));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    @Test
    void JSONTest() {
        System.out.println("000012.00001".startsWith("000012", "000012.00001".lastIndexOf(".")));
        System.out.println("张三.李四".startsWith("张四"));
        System.out.println("张三.李四".startsWith("张三", "张三.李四".lastIndexOf(".")));
    }


    @Test
    void downImage() {
        Blockchain testChain = new Blockchain();
        System.out.println("Mining block...");
        testChain.addBlock(new Block(1, System.currentTimeMillis(), "This is block 1"));
        System.out.println("Mining block...");
        testChain.addBlock(new Block(2, System.currentTimeMillis(), "This is block 2"));

        System.out.println("Is blockchain valid? " + testChain.checkValid());

    }

    @Test
    void getMaxLevel() {
        String[] levels = {"橙", "黄"};
        // 使用自定义比较器进行排序
        Arrays.sort(levels, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                // 定义颜色优先级
                int level1 = "红".equals(o1) ? 2 : ("橙".equals(o1) ? 1 : 0);
                int level2 = "红".equals(o2) ? 2 : ("橙".equals(o2) ? 1 : 0);

                // 返回比较结果
                return Integer.compare(level2, level1); // 反向排序以满足红>橙>黄
            }
        });
    }


    @Test
    void getDGTestData() {

        //20250407134012001
        //20250407134136002
        List<Long> psnList = ListUtils.newArrayList(20250407134012001L);

        for (Long psn : psnList) {

//            Date date = DateUtil.parseDateTime("2026-03-16 10:00:00");

            int value6001 = 0;//0-掘进；1-拼装；2-停机
            int value6002 = 10;
            int value6003 = 30;
            int value6004 = 40;
            int value6005 = 50;
            int value6006 = 65;
            double value6057 = 0.1;
            while (true/*new Date().after(date)*/) {
                Date date = new Date();
                JSONObject jsonObject = new JSONObject();
                JSONArray array = new JSONArray();
//                date = TimeUtil.addMinutes(date, new Random().nextInt(60*2));
                JSONObject psnJSON = new JSONObject();

                psnJSON.put("psn", psn);
                psnJSON.put("time", date.getTime());

                Random random = new Random();
                int randomInteger = random.nextInt(100);
                boolean flag = randomInteger == 1;
                JSONObject dataJSON = new JSONObject();
                dataJSON.put("6001", value6001);
                dataJSON.put("6002", flag ? value6002++ : value6002);
                dataJSON.put("6003", flag ? value6003++ : value6003);
                dataJSON.put("6004", flag ? value6004++ : value6004);
                dataJSON.put("6005", flag ? value6005++ : value6005);
                dataJSON.put("6006", flag ? value6006++ : value6006);
                dataJSON.put("6057", flag ? value6057++ : value6057);
                psnJSON.put("data", dataJSON);
                array.add(psnJSON);
                jsonObject.put("data", array);
                String url = "http://192.168.99.24:19075/receive-http-zhgd/stecSend";
                String response = HttpRequest.post(url)
                        .header("Content-Type", "application/json")
                        .body(jsonObject.toJSONString())
                        .execute()
                        .body();
                System.out.println(jsonObject.toJSONString());
                try {
                    //休眠2秒
                    new Thread().sleep(1000 * 30);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Test
    void getMonitorTestData() {

        List<Long> psnList = ListUtils.newArrayList(1603075739436001L);

        for (Long psn : psnList) {

            Date date = DateUtil.parseDateTime("2025-08-19 17:00:00");
            int value3001 = 10;//TSP
            int value3002 = 20;//噪音
            int value3003 = 30;//温度
            int value3004 = 40;//湿度
            int value3005 = 50;//风速
            int value3006 = 60;//风向
            while (new Date().after(date)) {
                JSONObject jsonObject = new JSONObject();
                JSONArray array = new JSONArray();
                date = TimeUtil.addMinutes(date, 5);
                JSONObject psnJSON = new JSONObject();

                psnJSON.put("psn", psn);
                psnJSON.put("time", date.getTime());

                Random random = new Random();
                int randomInteger = random.nextInt(5);
                boolean flag = randomInteger == 1;
                JSONObject dataJSON = new JSONObject();
//                dataJSON.put("3001", String.valueOf(value3001));
//                dataJSON.put("3002", String.valueOf(flag ? value3002++ : value3002));
//                dataJSON.put("3003", String.valueOf(flag ? value3003++ : value3003));
//                dataJSON.put("3004", String.valueOf(flag ? value3004++ : value3004));
//                dataJSON.put("3005", String.valueOf(flag ? value3005++ : value3005));
//                dataJSON.put("3006", String.valueOf(flag ? value3006++ : value3006));
                dataJSON.put("3001", value3001);
                dataJSON.put("3002", flag ? value3002++ : String.valueOf(value3002));
                dataJSON.put("3003", flag ? value3003++ : value3003);
                dataJSON.put("3004", flag ? value3004++ : value3004);
                dataJSON.put("3005", flag ? value3005++ : value3005);
                dataJSON.put("3006", flag ? value3006++ : value3006);
                psnJSON.put("data", dataJSON);
                array.add(psnJSON);
                jsonObject.put("data", array);
                String url = "http://localhost:19075/receive-http/stecSend";
                log.info("发送消息：{}", jsonObject.toJSONString());
                String response = HttpRequest.post(url)
                        .header("Content-Type", "application/json")
                        .body(jsonObject.toJSONString())
                        .execute()
                        .body();
            }
        }
    }

    @Test
    void testDateBuildUp() {
        ArrayList<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
        this.testAsync(999);
        for (Integer i : list) {
            System.out.println(i / i);
        }
    }

    @Async
    public void testAsync(Integer i) {
        try {
            Thread.sleep(5000L);
            System.out.println(i);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testIsNull() {

//        DateTime nextDeadline = DateUtil.parse("2024-12-25 00:00:00");
//        DateTime startDate = DateUtil.parse("2024-12-24 00:00:00");
//        long betweenDay = DateUtil.betweenDay(DateUtil.endOfDay(nextDeadline), startDate, true)+1;
//        System.out.println(betweenDay);
//
//        String a = DateUtil.formatDateTime(DateUtil.offsetHour(DateUtil.endOfDay(nextDeadline), -24));
//        System.out.println(a);
//        String b = DateUtil.formatDateTime(DateUtil.offsetHour(nextDeadline, -8));
//        System.out.println(b);
//        String c = DateUtil.formatDateTime(DateUtil.offsetHour(nextDeadline, -7));
//        System.out.println(c);

        // Step 1: Generate a random number between 0 and 999.
        Random random = new Random();
        int randomNumber = random.nextInt(1000); // Generates a number between 0 (inclusive) and 1000 (exclusive)
        System.out.println("The randomly generated number is: " + randomNumber);

        // Step 2: Use binary search to guess the number.
        int low = 0;
        int high = 999;
        int guesses = 0;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            guesses++;

            if (mid == randomNumber) {
                System.out.println("Found the number " + randomNumber + " after " + guesses + " guesses.");
                break;
            } else if (mid < randomNumber) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        if (low > high) {
            System.out.println("Number not found within the range. This should not happen in this scenario.");
        }


    }

    @Test
    void testNonNull() throws IOException {
        Map<String, Object> env = new HashMap<>();
        env.put("value", 0.0026666666666667);
//      String expression = "if (value >= 108) {return 'abc';} else {return 'ddd';}";

        String expression = "if (value <= 0.001) {return '优秀';}if (value <= 0.002) {return '良好';}if (value <= 0.005) {return '一般';} else{return '不合格';}";
        Expression compiledExp = AviatorEvaluator.compile(expression);
        Object obj = compiledExp.execute(env);
        System.out.println(obj);
    }

    private static final Set<String> TONGJI_PIPES = createPipes();

    private static Set<String> createPipes() {
        //同济推送数据监测项列表
        Set<String> set = new HashSet<>(Arrays.asList(
                "6730", "6731", "6732", "6733", "6734", "6735",
                "6736", "6737", "6746", "6747", "6748", "6749",
                "6769", "6770", "6751", "6767"
        ));
        return Collections.unmodifiableSet(set);
    }

    @Test
    void testValue() {

        Date startDate = DateUtil.parse("2024-01-01 00:00:00");
        Date endDate = DateUtil.parse("2024-01-01 09:00:00");

        //期望输出：2024-01-01 00:00 至 09:00
        //期望输出：2024-01-01 00:00 至 09:00
        String result = formatDateRange(startDate, endDate);
        log.info("日期范围：{}", result);

    }

    /**
     * 格式化日期范围
     * 如果开始和结束日期是同一天，格式为：yyyy-MM-dd HH:mm 至 HH:mm
     * 如果不是同一天，格式为：yyyy-MM-dd HH:mm 至 yyyy-MM-dd HH:mm
     */
    private String formatDateRange(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            return "";
        }

        DateTime startDateTime = DateUtil.date(startDate);
        DateTime endDateTime = DateUtil.date(endDate);

        // 判断是否为同一天
        boolean isSameDay = DateUtil.isSameDay(startDate, endDate);

        String startTime = DateUtil.format(startDateTime, "yyyy-MM-dd HH:mm");
        String endTime = DateUtil.format(endDateTime, "HH:mm");

        if (isSameDay) {
            return startTime + " 至 " + endTime;
        } else {
            String fullEndTime = DateUtil.format(endDateTime, "yyyy-MM-dd HH:mm");
            return startTime + " 至 " + fullEndTime;
        }
    }

    @Test
    void testExtUser() {
        String host = "https://kzidcardv1.market.alicloudapi.com";
        String path = "/api-mall/api/id_card/check";
        String method = "POST";
        String appcode = "c226da3be6f248ce8212b1db251cbc95";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();
        Map<String, String> bodys = new HashMap<>();

        bodys.put("name", "彭飞");
        bodys.put("idcard", "411123199105213511");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            if (ObjectUtils.isNotNull(response) && ObjectUtils.isNotNull(response.getEntity())) {
                String json = EntityUtils.toString(response.getEntity());
                JSONObject jsonObject = JSONObject.parseObject(json);
                if (ObjectUtils.isNotNull(jsonObject)) {
                    Integer code = jsonObject.getInteger("code");
                    if (!ObjectUtils.notEqual(code, 200)) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        Integer result = data.getInteger("result");
                        if (!ObjectUtils.notEqual(result, 0)) {
                            System.out.println("IDCardValidator.身份证实名认证通过！");
                        }
                    } else {
                        log.warn("IDCardValidator.身份证实名认证不通过！: {}", json);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}

