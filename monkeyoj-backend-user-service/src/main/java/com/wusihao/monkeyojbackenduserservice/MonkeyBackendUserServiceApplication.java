package com.wusihao.monkeyojbackenduserservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.wusihao.monkeyojbackenduserservice.mapper")
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@ComponentScan("com.wusihao")
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com/wusihao/monkeyojbackendserviceclient/service"})
public class MonkeyBackendUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyBackendUserServiceApplication.class, args);
    }

}
