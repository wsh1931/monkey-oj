package com.wusihao.monkeyojcodesandbox.unsafeCode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/4 11:30
 * @version: 1.0
 * @description: 无限占用空间（浪费系统内存）
 */
public class MemoryError {
    public static void main(String[] args) {
        List<byte[]> bytes = new ArrayList<>();
        while (true) {
            bytes.add(new byte[10000]);
        }
    }
}
