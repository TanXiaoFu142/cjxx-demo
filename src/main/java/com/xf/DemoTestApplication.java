package com.xf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages ="com.xf.mapper")
@ServletComponentScan(basePackages = "com.xf.filter")
public class DemoTestApplication {


    public static void main(String[] args) {
        SpringApplication.run(DemoTestApplication.class, args);
    }

}
