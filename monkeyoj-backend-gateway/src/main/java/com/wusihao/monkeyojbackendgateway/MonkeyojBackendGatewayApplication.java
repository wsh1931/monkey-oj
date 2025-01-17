package com.wusihao.monkeyojbackendgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
public class MonkeyojBackendGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonkeyojBackendGatewayApplication.class, args);
    }

}
