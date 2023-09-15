package com.xf.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.IoUtils;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.api.R;
import com.xf.entity.MasterEntity;
import com.xf.entity.PersonnelExcelDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * @ClassName MeasurementController
 * @Description 测量管理
 * @Author tanjunjie
 * @Date 2023/08/31 17:58
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/measurement", method = RequestMethod.GET)
public class MeasurementController {

    @RequestMapping(value = "/personnel",produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public void personnel(HttpServletResponse response) {

        Map<String, Object> map = new HashMap<>();
        map.put("name","张三");
        map.put("tendersName","机场线1标");
        map.put("projectOrgName","上海城建信息科技有限公司");
        map.put("sexInfo","男");
        map.put("workingYears","1");
        map.put("entryDate","2023-09-01");
        map.put("age","11");
        map.put("mobileNumber","12345678987");
        map.put("exitDate",null);
        String templateName = "measurement_personnel_templates";
        String templatesFilePath = "templates/measurement/measurement_personnel_templates.xlsx";
        this.export(map, templatesFilePath, response);
    }

    private void export(Map map, String templatesFilePath, HttpServletResponse response){
        try {
            //定义输出文件名称
            String fileName = URLEncoder.encode(map.get("name") + ".xlsx","UTF-8") ;
            //设置响应字符集
            response.setCharacterEncoding("UTF-8");
            //设置响应媒体类型
            response.setContentType("application/vnd.ms-excel");
            //设置响应的格式说明
            response.setHeader("Content-Disposition", "attachment;filename="+fileName);
            //读取响应文件的模板
            ClassPathResource resource = new ClassPathResource(templatesFilePath);
            File file= resource.getFile();
            //替换模板的数据
            EasyExcel.write(response.getOutputStream()).withTemplate(file).sheet().doFill(processPhoto(map));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理图片
     * @param map
     * @return
     */
    private Map<String, Object> processPhoto(Map<String, Object> map) {
        try {
            if (ObjectUtils.isNotNull(map)) {
                URL url = new URL("https://tech.suitbim.com/stec-platform-doc/img/wKhjGmTxTsiEdagyAAAAAIai5NI32.jpeg");
                InputStream inputStream = url.openStream();
                byte[] bytes = IoUtils.toByteArray(inputStream);
                WriteCellData<Object> cellData = new WriteCellData<>();
                List<ImageData> imageDataList = ListUtils.newArrayList();
                ImageData imageData = new ImageData();
                imageData.setImage(bytes);
                imageData.setTop(1);
                imageData.setRight(0);
                imageData.setBottom(1);
                imageData.setLeft(1);
                imageData.setRelativeFirstRowIndex(0);
                imageData.setRelativeFirstColumnIndex(0);
                imageData.setRelativeLastRowIndex(2);
                imageData.setRelativeLastColumnIndex(0);
                imageDataList.add(imageData);
                cellData.setImageDataList(imageDataList);
                map.put("photoUrl",cellData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }


    @ApiOperation("导出测量人员列表")
    @RequestMapping(value = "/exportList", produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public String exportList(HttpServletResponse response) {

        try {
            List<List> headList = new ArrayList<>();
            headList.add(Arrays.asList("姓名"));
            headList.add(Arrays.asList("所属标段"));
            headList.add(Arrays.asList("所属单位"));
            headList.add(Arrays.asList("进场日期"));
            headList.add(Arrays.asList("出场日期"));
            headList.add(Arrays.asList("人员状态"));
            headList.add(Arrays.asList("审核状态"));

//            PersonnelExcelDto dto = new PersonnelExcelDto();
//            dto.setName("张三");
//            dto.setStatus("1");
//            dto.setApprovalStatus("3");
//            dto.setEntryDate(new Date());
//            dto.setExitDate(new Date());
//            dto.setTendersName("机场线1标");
//            dto.setProjectOrgName("随便公司");
//
//            List<Object> dataList = new ArrayList<>();
//            dataList.add(dto);

            List<Object> dataList = new ArrayList<>();
//            dataList.add(Arrays.asList("1"));
            dataList.add(Arrays.asList("张三",1,1,1,1,1,3));
//            dataList.add(Arrays.asList("机场线1标"));
//            dataList.add(Arrays.asList("随便公司"));
//            dataList.add(Arrays.asList(new Date()));
//            dataList.add(Arrays.asList(new Date()));
//            dataList.add(Arrays.asList(1));
//            dataList.add(Arrays.asList(3));

            String fileName = "导出测量人员列表"+ExcelTypeEnum.XLSX.getValue();
            this.exportList(fileName, headList,dataList, response);
            return null;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
            return "失败！";
        }
    }

//    private void exportList(String fileName, List dataList, Class clazz, HttpServletResponse response) {
//        if (ObjectUtils.isNull(fileName, clazz, response) || CollectionUtil.isEmpty(dataList)) {
//            throw new RuntimeException("缺少文件名，配置信息，数据源！");
//        }
//        //导出操作
//        ExcelWriter writer = null;
//        try {
//            // 设置response参数，可以打开下载页面
//            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "attachment");
//            response.setHeader("attachment", URLEncoder.encode(fileName,  "utf-8"));
//
//            //内容样式
//            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
//            //内容居中
//            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            //头部样式
//            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
//            //头部标题居中
//            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
//            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
//
//            writer = EasyExcel.write(response.getOutputStream(),clazz).registerWriteHandler(horizontalCellStyleStrategy).build();
//            WriteSheet sheet = EasyExcel.writerSheet("查询结果").build();
//
//            writer.write(dataList, sheet);
//        } catch (Exception e) {
//            e.printStackTrace();
//            // 重置response
//            response.reset();
//            response.setContentType("application/json");
//            response.setCharacterEncoding("utf-8");
//        } finally {
//            writer.finish();
//        }
//    }

    private void exportList(String fileName,  List headList,List dataList, HttpServletResponse response) {
        if (ObjectUtils.isNull(fileName, response) || CollectionUtil.isEmpty(dataList) || CollectionUtil.isEmpty(headList)) {
            throw new RuntimeException("缺少文件名，配置信息，数据源！");
        }
        //导出操作
        ExcelWriter writer = null;
        try {
            // 设置response参数，可以打开下载页面
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, "attachment");
            response.setHeader("attachment", URLEncoder.encode(fileName,  "utf-8"));

            //内容样式
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            //内容居中
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //头部样式
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            //头部标题居中
            headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

            EasyExcel.write(response.getOutputStream())
                    // 这里放入动态头
                    .head(headList).sheet()
                    // 简单的列宽策略，列宽20
                    .registerWriteHandler(new SimpleColumnWidthStyleStrategy(20))
                    // 简单的行高策略：头行高30，内容行高20
                    .registerWriteHandler(new SimpleRowHeightStyleStrategy((short) 30, (short) 20))
                    //设置居中
                    .registerWriteHandler(horizontalCellStyleStrategy)
                    .doWrite(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
        } finally {
            writer.finish();
        }
    }
}
