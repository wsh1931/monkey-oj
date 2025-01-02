package com.yupi.monkeyoj.job.judge.codesandbox.impl;

import com.yupi.monkeyoj.job.judge.codesandbox.CodeSandbox;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:00
 * @version: 1.0
 * @description: 远程代码沙箱： 实际调用接口的沙箱
 */
public class RemoteSandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
