package com.xf.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @ClassName PersonSync
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/10/10 11:10
 * @Version 1.0
 */
@Slf4j
@Service("PersonSync")
@Configuration
public class PersonSync {
    private String token;

    public void handlePerson(){
        for (int i = 0; i < 10; i++) {
            String token =  this.getToken(false);
            System.out.println(token);
        }

        System.out.println("end=>"+token);
    }

    private String getToken(boolean force) {
        if (force || StringUtils.isEmpty(token)) {

            String str = String.valueOf(System.currentTimeMillis());
            System.out.println("str="+str);
            token = str;
        }
        return token;
    }
}
