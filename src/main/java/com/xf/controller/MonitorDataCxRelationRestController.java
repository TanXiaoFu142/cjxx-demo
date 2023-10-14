package com.xf.controller;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.xf.entity.EpImportModuleDto;
import com.xf.entity.MonitorDataCxRelationExcelTemplateDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName MonitorDataCxRelationRestController
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/09/14 16:56
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/monitorDataCxRelation", method = RequestMethod.GET)
public class MonitorDataCxRelationRestController {



    /**
     * 获取下拉框数据
     * @return
     */
    private MonitorDataCxRelationExcelTemplateDto getData() {

        MonitorDataCxRelationExcelTemplateDto dto = new MonitorDataCxRelationExcelTemplateDto();

        List<MonitorDataCxRelationExcelTemplateDto.TendersInfo> tendersInfoList = new ArrayList<>();
        List<MonitorDataCxRelationExcelTemplateDto.WorkPointInfo> workPointInfoList = new ArrayList<>();
        List<MonitorDataCxRelationExcelTemplateDto.MonitorManageInfo> monitorManageInfoList = new ArrayList<>();



        for (int i = 1; i <= 3; i++) {
            tendersInfoList.add(new MonitorDataCxRelationExcelTemplateDto.TendersInfo((long) i,i+"标"));
            if (i==2) {
                continue;
            }
            for (int j = 3; j <= 6; j++) {
                workPointInfoList.add(new MonitorDataCxRelationExcelTemplateDto.WorkPointInfo((long) i, (long) j,j+"工点"));
            }
        }


        monitorManageInfoList.add(new MonitorDataCxRelationExcelTemplateDto.MonitorManageInfo(1L+"#"+3L,"1标3工点基坑编码"));
        monitorManageInfoList.add(new MonitorDataCxRelationExcelTemplateDto.MonitorManageInfo(1L+"#"+3L,"1标3工点基坑编码"));
        monitorManageInfoList.add(new MonitorDataCxRelationExcelTemplateDto.MonitorManageInfo(3L+"#"+6L,"3标6工点基坑编码"));
        monitorManageInfoList.add(new MonitorDataCxRelationExcelTemplateDto.MonitorManageInfo(3L+"#"+4L,"3标4工点基坑编码"));



        dto.setTendersInfoList(tendersInfoList);
        dto.setWorkPointInfoList(workPointInfoList);
        dto.setMonitorManageInfoList(monitorManageInfoList);
        return dto;
    }

    @ApiOperation("导入模板下载")
    @RequestMapping(value = "/downloadTemplate",produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String downloadTemplate( HttpServletResponse response) {


        MonitorDataCxRelationExcelTemplateDto templateDto = this.getData();
        try {
            this.export(templateDto,response);
            return null;
        } catch (Exception e) {
            // 重置response
            response.reset();
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return "下载失败";
        }
    }

    private void export(MonitorDataCxRelationExcelTemplateDto templateDto, HttpServletResponse response) throws IOException {

        String formatDate = DateUtil.format(new Date(),DatePattern.CHINESE_DATE_TIME_PATTERN);
        String fileName = formatDate+"测斜类监测点编码与基坑分层数据关系表"  + ".xlsx";
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator +"monitor"+File.separator+ "monitor_data_cx_relation_plus.xlsx");

//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        response.setHeader("Access-control-Expose-Headers", "attachment");
//        response.setHeader("attachment", URLEncoder.encode(fileName, "utf-8"));

        //web测试
        //设置响应字符集
        response.setCharacterEncoding("UTF-8");
        //设置响应媒体类型
        response.setContentType("application/vnd.ms-excel");
        //设置响应的格式说明
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);

        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(inputStream).build();
        WriteSheet dataSheet = EasyExcel.writerSheet("data").build();

        //excel密码：cjxx

        //填充下拉框数据
        excelWriter.fill(templateDto.getTendersInfoList(), dataSheet);
        excelWriter.fill(templateDto.getWorkPointInfoList(), dataSheet);
        excelWriter.fill(templateDto.getMonitorManageInfoList(),dataSheet);

        excelWriter.finish();
        File file = new File(fileName);
        FileInputStream fis = new FileInputStream(file);
        byte[] buffer = new byte[1024];
        int len = 0;
        //循环将输入流中的内容读取到缓冲区当中
        while ((len = fis.read(buffer)) > 0) {
            //输出缓冲区的内容到浏览器，实现文件下载
            out.write(buffer, 0, len);
        }
        //关闭文件输入流
        fis.close();
        //关闭输出流
        out.close();
        // 删除缓存文件
        if (file.exists()) {
            file.delete();
        }
    }

}
