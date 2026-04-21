package com.xf.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.SimpleColumnWidthStyleStrategy;
import com.alibaba.excel.write.style.row.SimpleRowHeightStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.stec.utils.ImageRotateUtils;
import com.xf.entity.MasterEntity;
import com.xf.service.KfcService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.java.Log;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 肖俊杰
 * @date 2021/12/10
 * @time 11:22
 */
@RestController
public class ThankController {

    private static final Logger log = LoggerFactory.getLogger(ThankController.class);
    @Autowired
    private KfcService kfcService;

//    private KfcService kfcService;
//    @Autowired
//    public void KfcService(KfcService kfcService){
//        this.kfcService = null;
//    }

    @RequestMapping("/hello")
    public String hello(){
        try {
            System.out.println(1/0);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
        return "Hello World";
    }

    @RequestMapping(value = "/hello23/{id}",method = RequestMethod.GET)
    public Integer hello23(@PathVariable("id") @RequestBody Integer id){

        System.out.println(id);
        return id;
    }

    @RequestMapping(value = "/hello45",method = RequestMethod.POST)
    public String hello45(@RequestBody MasterEntity myModel) {
        System.out.println("Received MyModel: " + myModel);
        return myModel.toString();
    }

    @RequestMapping(value = "/hello67",method = RequestMethod.POST)
    public String hello45(@RequestBody List<MasterEntity> myModelList) {
        System.out.println("Received MyModel: " + myModelList);
        return myModelList.toString();
    }

    @RequestMapping(value = "/getOk",method = RequestMethod.GET)
    public String getOk(){
        try {
            String ok = kfcService.getOk();
//        kfcService.getOkOk();
            return ok;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }



    @RequestMapping(value = "/testOk",method = RequestMethod.GET)
    public String testOk(){
//        boolean flag = ImageRotateUtils.judgeRotate("D:\\Users\\Downloads\\新建文本文档.txt");
        boolean flag = ImageRotateUtils.judgeRotate("D:\\Users\\Downloads\\监管发送内容.gif");

        log.warn("进来了");
        return "ok";
    }

    @RequestMapping(value = "/upOk",method = RequestMethod.GET)
    public String upOk(MultipartRequest multipartRequest, MasterEntity entity){
        System.out.println("1111");
        return "ok";
    }

}
