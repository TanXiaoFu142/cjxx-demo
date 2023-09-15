package com.xf.controller;

import com.xf.service.KfcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 肖俊杰
 * @date 2021/12/10
 * @time 11:22
 */
@RestController
public class ThankController {

    @Autowired
    private KfcService kfcService;

//    private KfcService kfcService;
//    @Autowired
//    public void KfcService(KfcService kfcService){
//        this.kfcService = null;
//    }

    @RequestMapping("/hello")
    public String hello(){
        return "Hello World";
    }

    @RequestMapping(value = "/hello23/{:id}",method = RequestMethod.GET)
    public Integer hello23(@PathVariable(":id") Integer id){

        System.out.println(id);
        return id;
    }

    @RequestMapping(value = "/getOk",method = RequestMethod.GET)
    public String getOk(){
        String ok = kfcService.getOk();
        kfcService.getOkOk();
        return ok;
    }
}
