package com.xf.service.impl;

import com.xf.exception.DataServiceException;
import com.xf.service.KfcService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @ClassName KfcServiceImpl
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/06/25 15:09
 * @Version 1.0
 */
@Service
public class KfcServiceImpl implements KfcService {
//    @Async
    @Override
    public String getOk() {
        try {
            System.out.println(1/0);
        } catch (Exception e) {
            throw new DataServiceException("不能除以0");
        }
        return "ok";
    }

    @Async
    @Override
    public void getOkOk(){
        for (int i = 1000000; i > 0; i--) {
            System.out.println(i+"OkOk!!!");
        }
    }
}
