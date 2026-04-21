package com.xf;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stec.utils.CollectionUtils;
import com.stec.utils.ListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author tanjunjie
 * @version 1.0
 * @className ExcelTest
 * @date 2025/7/16 15:59
 * @description TODO
 */
@Slf4j
@SpringBootTest
public class ExcelTest {


    /**
     * 不创建对象的写
     */
    @Test
    public void noModelWrite() {
        // 写法1
        String fileName = "noModelWrite" + System.currentTimeMillis() + ".xlsx";


        //获取动态头部
        List<List<String>> headList = this.getHeadList();

        //获取动态数据源
        List<List<Object>> dataList = this.getDataList();

        //列宽
        Integer columnWide = 20;
        //头行高
        Short headHigh = 30;
        //内容行高
        Short contentLineHeight = 20;
        //内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //内容居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        //头部样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        //头部标题居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        EasyExcel.write(fileName)
                // 这里放入动态头
                .head(headList).sheet()
                // 简单的列宽策略，列宽20
                .registerWriteHandler(new SimpleColumnWidthStyleStrategy(columnWide))
                // 简单的行高策略：头行高30，内容行高20
                .registerWriteHandler(new SimpleRowHeightStyleStrategy(headHigh, contentLineHeight))
                //设置居中
                .registerWriteHandler(horizontalCellStyleStrategy)
                .doWrite(dataList);
    }


    private List<List<String>> getHeadList() {
        List<List<String>> headList = ListUtils.newArrayList();
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "指标", "指标"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "累计", "累计"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "1月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "2月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "3月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "4月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "5月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "6月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "7月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "8月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "9月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "10月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "11月"));
        headList.add(ListUtils.newArrayList("2024-2025年投资完成情况表", "2025年", "12月"));
        return headList;
    }

    private List<List<Object>> getDataList() {
        List<List<Object>> resultList = ListUtils.newArrayList();

        //数据库查询数据
        List<JSONObject> jsonList = this.getJsonList();
        if (CollectionUtils.isNotEmpty(jsonList)) {

            List<Integer> yearList = ListUtils.newArrayList(2024, 2025);
            List<Integer> monthList = ListUtils.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

            Map<Integer, Map<Integer, JSONObject>> yearMap = jsonList.stream()
                    .collect(Collectors.groupingBy(
                            json -> MapUtils.getInteger(json, "year"),
                            Collectors.toMap(json -> MapUtils.getInteger(json, "month"), json -> json)));
            //当月合同签订总金额
            //当月完成计量产值(投资合同)
            //当月完成计量产值(总包合同)
            //当月完成用款
            //当月外部结算
            //当月内部决算
            List<String> rowList = ListUtils.newArrayList("当月合同签订总金额", "当月完成计量产值(投资合同)", "当月完成计量产值(总包合同)", "当月完成用款", "当月外部结算", "当月内部决算");
            for (String str : rowList) {
                List<Object> datas = ListUtils.newArrayList(str);
                for (Integer year : yearMap.keySet()) {
                    //累计
                    datas.add(1000);
                    //1月-12月
                    Map<Integer, JSONObject> monthMap = yearMap.get(year);
                    datas.add(monthMap.get(1).get("month"));
                    datas.add(monthMap.get(2).get("month"));
                    datas.add(monthMap.get(3).get("month"));
                    datas.add(monthMap.get(4).get("month"));
                    datas.add(monthMap.get(5).get("month"));
                    datas.add(monthMap.get(6).get("month"));
                    datas.add(monthMap.get(7).get("month"));
                    datas.add(monthMap.get(8).get("month"));
                    datas.add(monthMap.get(9).get("month"));
                    datas.add(monthMap.get(10).get("month"));
                    datas.add(monthMap.get(11).get("month"));
                    datas.add(monthMap.get(12).get("month"));
                }
                resultList.add(datas);
            }
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
        File file = new File("src/main/resources/templates/jsonFile/fenghuatouzi.json");
        try {
            String content = FileUtils.readFileToString(file, "UTF-8");
            JSONArray jsonArray = JSONArray.parseArray(content);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                resultList.add(jsonObject);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultList;
    }



}
