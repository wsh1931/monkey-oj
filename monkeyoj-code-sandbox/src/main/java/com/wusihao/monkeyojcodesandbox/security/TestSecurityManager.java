package com.wusihao.monkeyojcodesandbox.security;

import cn.hutool.core.io.FileUtil;

import java.nio.charset.StandardCharsets;

public class TestSecurityManager {
    public static void main(String[] args) {
        System.setSecurityManager(new MySecurityManager());
        FileUtil.readLines("D:\\JAVA\\project\\monkey-oj\\monkeyoj-code-sandbox\\src\\main\\resources\\testCode\\unsafeCode\\MemoryError.java", StandardCharsets.UTF_8);
//        FileUtil.writeString("aa","aaa", Charset.defaultCharset());
    }
}