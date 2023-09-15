package com.xf.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.xf.entity.EpImportModuleDto;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName EpPersonContriller
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/09/05 17:39
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping(value = "/rest/measurement", method = RequestMethod.GET)
public class EpPersonContriller {


    /**
     * 获取下拉框数据
     * @return
     */
    private EpImportModuleDto getData() {
        EpImportModuleDto dto = new EpImportModuleDto();

        //性别数据
        List<EpImportModuleDto.GenderDicDto> genderDicDtoList = new ArrayList<>();
        genderDicDtoList.add(new EpImportModuleDto.GenderDicDto("male", "男"));
        genderDicDtoList.add(new EpImportModuleDto.GenderDicDto("female", "女"));
        dto.setGenderDicDtoList(genderDicDtoList);


        //标段数据
        List<EpImportModuleDto.TendersDicDto> tendersDicDtoList = new ArrayList<>();
        tendersDicDtoList.add(new EpImportModuleDto.TendersDicDto(11L, "机场线1标"));
        tendersDicDtoList.add(new EpImportModuleDto.TendersDicDto(12L, "机场线2标"));
        tendersDicDtoList.add(new EpImportModuleDto.TendersDicDto(13L, "机场线3标"));
        tendersDicDtoList.add(new EpImportModuleDto.TendersDicDto(14L, "机场线4标"));
        tendersDicDtoList.add(new EpImportModuleDto.TendersDicDto(15L, "机场线5标"));
        dto.setTendersDicDtoList(tendersDicDtoList);

        //人员类型数据
        List<EpImportModuleDto.PersonnelTypeDicDto> personnelTypeDicDtoList = new ArrayList<>();
        personnelTypeDicDtoList.add(new EpImportModuleDto.PersonnelTypeDicDto("1", "管理人员"));
        personnelTypeDicDtoList.add(new EpImportModuleDto.PersonnelTypeDicDto("2", "劳务人员"));
        dto.setPersonnelTypeDicDtoList(personnelTypeDicDtoList);

        //工种类型数据
        List<EpImportModuleDto.WorkTypeDicDto> workTypeDicDtoList = new ArrayList<>();
        workTypeDicDtoList.add(new EpImportModuleDto.WorkTypeDicDto("1", "工种1"));
        workTypeDicDtoList.add(new EpImportModuleDto.WorkTypeDicDto("2", "工种2"));
        workTypeDicDtoList.add(new EpImportModuleDto.WorkTypeDicDto("3", "工种3"));
        workTypeDicDtoList.add(new EpImportModuleDto.WorkTypeDicDto("4", "工种4"));
        dto.setWorkTypeDicDtoList(workTypeDicDtoList);

        //劳务公司数据
        List<EpImportModuleDto.OrgDicDto> orgDicDtoList = new ArrayList<>();
        orgDicDtoList.add(new EpImportModuleDto.OrgDicDto(1L, "单位1"));
        orgDicDtoList.add(new EpImportModuleDto.OrgDicDto(2L, "单位2"));
        orgDicDtoList.add(new EpImportModuleDto.OrgDicDto(3L, "单位3"));
        orgDicDtoList.add(new EpImportModuleDto.OrgDicDto(4L, "单位4"));
        orgDicDtoList.add(new EpImportModuleDto.OrgDicDto(5L, "单位5"));
        dto.setOrgDicDtoList(orgDicDtoList);

        //工作状态数据
        List<EpImportModuleDto.WorkStateDicDto> workStateDicDtoList = new ArrayList<>();
        workStateDicDtoList.add(new EpImportModuleDto.WorkStateDicDto("1", "在工"));
        workStateDicDtoList.add(new EpImportModuleDto.WorkStateDicDto("2", "待确认进场"));
        workStateDicDtoList.add(new EpImportModuleDto.WorkStateDicDto("3", "离开"));
        dto.setWorkStateDicDtoList(workStateDicDtoList);

        //岗位数据 post_name
        List<EpImportModuleDto.PostCodeDicDto> postCodeDicDtoList = new ArrayList<>();
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("1", "岗位1"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("2", "岗位2"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("3", "岗位3"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("4", "岗位4"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("5", "岗位5"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("6", "岗位6"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("7", "岗位7"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("8", "岗位8"));
        postCodeDicDtoList.add(new EpImportModuleDto.PostCodeDicDto("9", "岗位9"));
        dto.setPostCodeDicDtoList(postCodeDicDtoList);

        //管理类型 manager_type
        List<EpImportModuleDto.ManagerTypeDicDto> managerTypeDicDtoList = new ArrayList<>();
        managerTypeDicDtoList.add(new EpImportModuleDto.ManagerTypeDicDto("JS", "建设"));
        managerTypeDicDtoList.add(new EpImportModuleDto.ManagerTypeDicDto("ZB", "总包"));
        managerTypeDicDtoList.add(new EpImportModuleDto.ManagerTypeDicDto("JL", "监理"));
        dto.setManagerTypeDicDtoList(managerTypeDicDtoList);

        return dto;
    }

    @ApiOperation("下载模板")
    @RequestMapping("/downloadTemplateLw")
    public String downLoadEpModuleLw(HttpServletResponse response) throws IOException {
        String templateName = "ep_input_template_lw";
        String fileName = URLEncoder.encode("人员信息导入模板"+ DateUtil.formatDateTime(DateUtil.date())+ExcelTypeEnum.XLSX.getValue());
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator +"ep"+File.separator+ templateName+ ExcelTypeEnum.XLSX.getValue());

        //web测试
        //设置响应字符集
        response.setCharacterEncoding("UTF-8");
        //设置响应媒体类型
        response.setContentType("application/vnd.ms-excel");
        //设置响应的格式说明
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);

//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        response.setHeader("Access-control-Expose-Headers", "attachment");
//        response.setHeader("attachment", URLEncoder.encode(fileName, "utf-8"));

        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(inputStream).build();
        //保护工作表密码：cjxx
        WriteSheet dataSheet = EasyExcel.writerSheet("data").build();

        EpImportModuleDto templateDto = this.getData();
        //填充下拉框数据

        //性别
        excelWriter.fill(templateDto.getGenderDicDtoList(), dataSheet);
        //标段信息
        excelWriter.fill(templateDto.getTendersDicDtoList(), dataSheet);
        //人员类型信息
        excelWriter.fill(templateDto.getPersonnelTypeDicDtoList(), dataSheet);
        //工种类型
        excelWriter.fill(templateDto.getWorkTypeDicDtoList(), dataSheet);
        //管理类型
        excelWriter.fill(templateDto.getManagerTypeDicDtoList(), dataSheet);
        //岗位类型
        excelWriter.fill(templateDto.getPostCodeDicDtoList(),dataSheet);

        //所属单位
        excelWriter.fill(templateDto.getOrgDicDtoList(), dataSheet);
        //工作状态
        excelWriter.fill(templateDto.getWorkStateDicDtoList(), dataSheet);


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
        return "成功！";
    }

    @ApiOperation("下载模板")
    @RequestMapping("/downloadTemplateGl")
    public String downLoadEpModuleGl(HttpServletResponse response) throws IOException{
        String templateName = "ep_input_template_gl";
        String fileName = URLEncoder.encode("人员信息导入模板"+ DateUtil.formatDateTime(DateUtil.date())+ExcelTypeEnum.XLSX.getValue());
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("templates" + File.separator +"ep"+File.separator+ templateName+ ExcelTypeEnum.XLSX.getValue());

        //web测试
        //设置响应字符集
        response.setCharacterEncoding("UTF-8");
        //设置响应媒体类型
        response.setContentType("application/vnd.ms-excel");
        //设置响应的格式说明
        response.setHeader("Content-Disposition", "attachment;filename="+fileName);

//        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
//        response.setHeader("Access-control-Expose-Headers", "attachment");
//        response.setHeader("attachment", URLEncoder.encode(fileName, "utf-8"));

        OutputStream out = response.getOutputStream();
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(inputStream).build();
        //保护工作表密码：cjxx
        WriteSheet dataSheet = EasyExcel.writerSheet("data").build();

        EpImportModuleDto templateDto = this.getData();
        //填充下拉框数据

        //性别
        excelWriter.fill(templateDto.getGenderDicDtoList(), dataSheet);
        //标段信息
        excelWriter.fill(templateDto.getTendersDicDtoList(), dataSheet);
        //人员类型信息
        excelWriter.fill(templateDto.getPersonnelTypeDicDtoList(), dataSheet);
        //工种类型
        excelWriter.fill(templateDto.getWorkTypeDicDtoList(), dataSheet);
        //管理类型
        excelWriter.fill(templateDto.getManagerTypeDicDtoList(), dataSheet);
        //岗位类型
        excelWriter.fill(templateDto.getPostCodeDicDtoList(),dataSheet);

        //所属单位
        excelWriter.fill(templateDto.getOrgDicDtoList(), dataSheet);
        //工作状态
        excelWriter.fill(templateDto.getWorkStateDicDtoList(), dataSheet);


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
        return "成功！";
    }
}
