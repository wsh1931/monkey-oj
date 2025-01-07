package com.wusihao.monkeyojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: wusihao
 * @date: 2025/1/3 11:25
 * @version: 1.0
 * @description:
 */
@RestController("/")
public class MainController {
    @GetMapping("/health")
    public String healthCheck() {
        return "测试";
    }
}
