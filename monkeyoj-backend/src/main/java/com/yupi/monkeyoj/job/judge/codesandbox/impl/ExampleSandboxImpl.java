package com.yupi.monkeyoj.job.judge.codesandbox.impl;

import com.yupi.monkeyoj.job.judge.codesandbox.CodeSandbox;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:00
 * @version: 1.0
 * @description: 示例代码沙箱
 */
public class ExampleSandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("示例代码沙箱");
        return null;
    }
}
