package com.xf.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xf.config.MergeStrategy;
import com.xf.entity.DbsReportsData;
import com.xf.entity.DbsSituationData;
import com.xf.entity.DbsWeekReport;
import com.xf.entity.EpImportModuleDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName DbsWeekReportController
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/10/14 15:40
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/dbsWeekReport", method = RequestMethod.GET)
public class DbsWeekReportController {

    /**
     * 获取数据源
     *
     * @return
     */
    private List<DbsWeekReport> getData() {

        //BIM周月报
        List<DbsWeekReport> weekReportList = new ArrayList<>();
        DbsWeekReport dbsWeekReport = new DbsWeekReport();

        //BIM周月报标题
        List<DbsReportsData> dbsReportsDataList = new ArrayList<>();
        DbsReportsData dbsReportsData1 = new DbsReportsData();
        dbsReportsData1.setName("一、上周工作完成情况（8.20-8.27）");
        dbsReportsData1.setStartDate("2023-10-16");
        dbsReportsData1.setEndDate("2023-10-16");
        //BIM应用基础数据
        List<DbsSituationData> dbsSituationDataList1 = this.getDbsSituationDataList();
        dbsReportsData1.setSituationDataList(dbsSituationDataList1);
        dbsReportsDataList.add(dbsReportsData1);

        //BIM周月报标题
        DbsReportsData dbsReportsData2 = new DbsReportsData();

        dbsReportsData2.setName("二、本周工作计划（8.27-9.3）");
        dbsReportsData2.setStartDate("2022-10-16");
        dbsReportsData2.setEndDate("2022-10-16");
        //BIM应用基础数据
        List<DbsSituationData> dbsSituationDataList2 = this.getDbsSituationDataList();
        dbsReportsData2.setSituationDataList(dbsSituationDataList2);
        dbsReportsDataList.add(dbsReportsData2);


        //BIM周月报标题
        DbsReportsData dbsReportsData3 = new DbsReportsData();
        dbsReportsData3.setName("三、需协调解决问题");
        dbsReportsData3.setStartDate("2021-10-16");
        dbsReportsData3.setEndDate("2021-10-16");
        //BIM应用基础数据
        List<DbsSituationData> dbsSituationDataList3 = this.getDbsSituationDataList();
        dbsReportsData3.setSituationDataList(dbsSituationDataList3);
        dbsReportsDataList.add(dbsReportsData3);

        dbsWeekReport.setName("头部标题");
        dbsWeekReport.setDbsReportsDataList(dbsReportsDataList);
        weekReportList.add(dbsWeekReport);

        DbsWeekReport dbsWeekReport1 = new DbsWeekReport();
        BeanUtils.copyProperties(dbsWeekReport, dbsWeekReport1);
        dbsWeekReport1.setName("1111");
        weekReportList.add(dbsWeekReport1);
        return weekReportList;
    }

    /**
     * 模拟BIM周报数据
     *
     * @return
     */
    private List<DbsSituationData> getDbsSituationDataList() {
        //BIM应用基础数据
        List<DbsSituationData> dbsSituationDataList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            DbsSituationData dbsSituationData = new DbsSituationData();
            dbsSituationData.setWorkContentUnlimited("工作内容：提供长虹站、牛塘站施工范围线" + i);
            dbsSituationData.setCompleteSituation("完成情况：已完成" + i);
            dbsSituationData.setResponsible("责任人：胡正波" + i);
            dbsSituationData.setRemark("备注"+i);
            dbsSituationData.setTendersName("标段名称"+i);
            dbsSituationData.setJobType("工作类型："+i);
            dbsSituationData.setCompleteWorkMonth("本月完成工作："+i);
            dbsSituationData.setOriginalPlannedCompletionTime(new Date());
            dbsSituationData.setWorkCompleted("完成工作情况"+i);
            dbsSituationData.setAdjustLaterPlans(i%2==0 ? "是":"否");
            dbsSituationData.setNextStageWorkPlan("下阶段工作计划"+i);
            dbsSituationData.setPlannedCompletionTime(new Date());
            dbsSituationData.setAdjustPlan(i%2==0 ? "是":"否");
            dbsSituationData.setQuestionContent("问题内容："+i);
            dbsSituationDataList.add(dbsSituationData);
        }
        return dbsSituationDataList;
    }

    /**
     * 导出周报
     * @param fileName
     * @param dataList
     * @param response
     */
    private void exportList(String fileName,ClassPathResource resource, List<DbsWeekReport> dataList, HttpServletResponse response) {
        ExcelWriter excelWriter = null;
        InputStream inputStream;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            // 设置response参数，可以打开下载页面
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "attachment");
            response.setHeader("attachment", URLEncoder.encode(fileName, "utf-8"));

            inputStream = resource.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // 设置模板的第一个sheet的名称
            workbook.setSheetName(0, dataList.get(0).getName());
            for (int i = 1; i < dataList.size(); i++) {
                // 剩余的全部复制模板sheet0即可
                workbook.cloneSheet(0, dataList.get(i).getName());
            }
            // 把workbook写到流里
            workbook.write(baos);
            byte[] bytes = baos.toByteArray();
            inputStream = new ByteArrayInputStream(bytes);
            excelWriter = EasyExcel.write(response.getOutputStream()).withTemplate(inputStream).registerWriteHandler(new MergeStrategy()).build();

            for (DbsWeekReport dbsWeekReport : dataList) {
                WriteSheet writeSheet = EasyExcel.writerSheet(dbsWeekReport.getName()).build();

                // list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
                FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).direction(WriteDirectionEnum.VERTICAL).build();

                //标题
                List<DbsReportsData> dbsReportsDataList = dbsWeekReport.getDbsReportsDataList();


//                Map<String, Object> map = new HashMap<>();
//                map.put("projectName", "常州BIM周报");
//                map.put("projectOrgName","铁四院");

                Map<String, Object> map = new HashMap<>();
                map.put("projectName", "常州BIM月报");
                map.put("projectOrgName","铁四院");

                for (int i = 0; i < dbsReportsDataList.size(); i++) {
                    DbsReportsData dbsReportsData = dbsReportsDataList.get(i);

                    map.put("date"+i,dbsReportsData.getStartDate()+"~"+dbsReportsData.getEndDate());

                    //标题下的内容
                    List<DbsSituationData> situationDataList = dbsReportsData.getSituationDataList();
                    List<Object> dtoList = new ArrayList<>();
                    if (CollectionUtil.isNotEmpty(situationDataList)) {
                        for (int j = 0; j < situationDataList.size(); j++) {
                            DbsSituationData dbsSituationData = situationDataList.get(j);
                            Map<String, Object> dtoMap = BeanUtil.beanToMap(dbsSituationData);
                            //添加递增序号
                            dtoMap.put("index",j+1);
                            dtoList.add(dtoMap);
                        }
                    }
                    excelWriter.fill(new FillWrapper("data" + i, dtoList), fillConfig, writeSheet);
                }
                excelWriter.fill(map, writeSheet);
            }

        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (ObjectUtils.isNotNull(excelWriter)) {
                excelWriter.finish();
            }
        }
    }

    @ApiOperation("导出周报月报")
    @RequestMapping(value = "/export", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String exportDbsWeekMonthReport(HttpServletResponse response) throws IOException {

        String fileName = "导出的文件名称" + ExcelTypeEnum.XLSX.getValue();

        //获取数据源
        List<DbsWeekReport> dataList = this.getData();

        //周报模版
//        ClassPathResource classPathResourceWeek = new ClassPathResource("templates" + File.separator + "dbs" + File.separator + "bim_week_template" + ExcelTypeEnum.XLSX.getValue());
//        this.exportList(fileName,classPathResourceWeek,dataList,response);

        //月报模版
        ClassPathResource classPathResourceMonth = new ClassPathResource("templates" + File.separator + "dbs" + File.separator + "bim_month_template" + ExcelTypeEnum.XLSX.getValue());
        this.exportList(fileName,classPathResourceMonth,dataList,response);



        return "ok";
    }
}
