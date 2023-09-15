package com.xf.controller;

import com.sun.javafx.runtime.async.AbstractAsyncOperation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @ClassName SuitContriller
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/05/25 16:50
 * @Version 1.0
 */
@Slf4j
@Api(tags = "EasyExcel")
@RestController
@RequestMapping(value = "/rest/suit")
public class SuitController {
    @ApiOperation("导出Excel")
    @RequestMapping(method = RequestMethod.GET,value = "/export")
    public String export() {
        return "导出成功！";
    }

    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String getUser(){
        return "GET-张三";
    }

    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String saveUser(){
        return "POST-张三";
    }


    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    public String putUser(){
        return "PUT-张三";
    }

    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    public String deleteUser(){
        return "DELETE-张三";
    }

//    @PostMapping("/triggerTasks")
//    public Result triggerTasks(@RequestBody Map<String,String> paramMap) throws InterruptedException {
//        // 创建线程池
//        ExecutorService executor = Executors.newFixedThreadPool(12);
//
//        // 创建任务列表
//        List<Callable<Integer>> tasks = new ArrayList<>();
//        for (int i = 1; i <= 12; i++) {
//            final int month = i;
////            Callable<Integer> task = () -> {
////                // 查询每个月的总计订单数
////                int totalOrders = queryTotalOrders(month);
////                return totalOrders;
////            };
//
//            Callable<Integer> task = new Callable<Integer>() {
//                @Override
//                public Integer call() throws Exception {
//                    return queryTotalOrders(month);;
//                }
//            };
//
//
//            tasks.add(task);
//        }
//
//        try {
//            // 提交任务并获取Future对象
//            List<Future<Integer>> futures = executor.invokeAll(tasks);
//
//            // 获取任务的执行结果并汇总
//            int totalOrders = 0;
//            for (Future<Integer> future : futures) {
//                totalOrders += future.get();
//            }
//
//            System.out.println("Total orders: " + totalOrders);
//
//        } catch (InterruptedException | ExecutionException e){
//
//        }
//        return Result.successResult();
//    }


}
