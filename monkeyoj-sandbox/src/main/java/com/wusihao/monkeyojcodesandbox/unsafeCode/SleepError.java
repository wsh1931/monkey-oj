package com.wusihao.monkeyojcodesandbox.unsafeCode;

/**
 * @author: wusihao
 * @date: 2025/1/4 11:16
 * @version: 1.0
 * @description: 无限睡眠错误示例代码
 */
public class SleepError {
    public static void main(String[] args) throws InterruptedException {
        long LONG_HOUR = 60 * 60 * 1000L;
        Thread.sleep(LONG_HOUR);
        System.out.println("睡完了");
    }
}
