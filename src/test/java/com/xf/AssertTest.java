package com.xf;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @ClassName AssertTest
 * @Description TODO
 * @Author tanjunjie
 * @Date 2023/07/19 10:53
 * @Version 1.0
 */
@SpringBootTest
public class AssertTest {

    @Test
    void test1(){
        String name = "abner chai";
//        String name = null;
        assert (name!=null):"变量name为空null";
        System.out.println(name);
    }
}
