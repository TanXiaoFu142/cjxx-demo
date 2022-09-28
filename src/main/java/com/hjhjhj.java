package com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 谭俊杰
 * @date 2022/8/11
 * @time 15:24
 */
public class hjhjhj {

    /**
     * 获取两个日期之间的所有月份 (年月)
     *
     * @param startTime
     * @param endTime
     * @return：YYYY-MM
     */
    public static List<String> getMonthBetweenDate(String startTime, String endTime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = sdf.parse(startTime);
            Date endDate = sdf.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime()<=endDate.getTime()){
                // 把日期添加到集合
                list.add(sdf.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.MONTH, 1);
                // 获取增加后的日期
                startDate=calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {
        String startStr = "2022-07-15";
        String endStr = "2022-08-14";
        List<String> attendanceMonth = getMonthBetweenDate(startStr, endStr);
        List<String> monthBetweenDate = getMonthBetweenDate("2022-08-01", "2022-08-10");

        System.out.println("attendanceMonth："+attendanceMonth);
        System.out.println("monthBetweenDate："+monthBetweenDate);
        Iterator<String> iterator = monthBetweenDate.iterator();
        while (iterator.hasNext()) {
            String yearMonth = iterator.next();
            if (!attendanceMonth.contains(yearMonth)) {
                iterator.remove();
            }
        }
        System.out.println(monthBetweenDate);
    }
}
