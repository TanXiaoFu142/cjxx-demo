package com.xf;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @author tanjunjie
 * @version 1.0
 * @className FutureTest
 * @date 2024/7/23 14:11
 * @description TODO
 */
@Slf4j
@SpringBootTest
public class CompletableFutureTest {

    /**
     * 获取智慧工地数据
     * @param projectId 项目ID
     * @return
     */
    public static Map<String,Object> getZhgdData(Long projectId){
        if (ObjectUtils.isEmpty(projectId)) {
            throw new NullPointerException("项目ID不能为空！");
        }

        Map<String, Object> resultMap = new HashMap<>();
        //1-10的随机整数
        int random = (int) (Math.random() * 10 + 1);
        try {
            Thread.sleep(random * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("休眠时间:{},秒",random);
        resultMap.put("休眠时间",random);
        return resultMap;
    }

    @Test
    void supplyAsyncTest() throws ExecutionException, InterruptedException {
//        CompletableFuture<String> future = CompletableFuture.completedFuture("返回一个已经使用给定值完成的新的 CompletableFuture。");
//        System.out.println(future.get());

//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "返回一个新的 CompletableFuture，" +
//                "该Future 由ForkJoinPool. commonPool()中运行的任务异步完成，并通过调用给定的 Supplier 获取值。");

//        Executor executor = Executors.newSingleThreadExecutor();
//        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> "Hello", executor);

        //业务使用场景

        Executor executor = Executors.newSingleThreadExecutor();
        CompletableFuture<Map<String, Object>> future = CompletableFuture.supplyAsync(() -> {
            Map<String, Object> zhgdData = getZhgdData(1L);
            return zhgdData;
        }, executor);

        Map<String, Object> map = future.get();

        for (String key : map.keySet()) {
            log.info("key:{},value:{}",key,map.get(key));
        }


    }
}
