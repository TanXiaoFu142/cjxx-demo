package com.xf.service.impl;

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
        return "ok";
    }

    @Async
    @Override
    public void getOkOk(){
        for (int i = 1000000; i > 0; i--) {
            System.out.println("OkOk!!!");
        }
    }
}
