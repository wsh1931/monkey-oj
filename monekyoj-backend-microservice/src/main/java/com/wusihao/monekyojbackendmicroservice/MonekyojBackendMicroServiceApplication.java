package com.wusihao.monekyojbackendmicroservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wusihao.monekyojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wusihao")
public class MonekyojBackendMicroServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonekyojBackendMicroServiceApplication.class, args);
    }

}
