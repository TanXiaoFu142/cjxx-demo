package com.xf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.stec.utils.ObjectUtils;
import com.stec.utils.TimeUtil;
import com.xf.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.*;

/**
 * @author tanjunjie
 * @version 1.0
 * @className StecTools
 * @date 2026/5/13 17:42
 * @description TODO
 */
@Slf4j
@EnableScheduling
@SpringBootTest
public class StecTools {

    /**
     * 身份证实名认证
     */
    @Test
    void testIdCardVerify() {
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

    /**
     * 模拟监测数据推送
     */
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



}
