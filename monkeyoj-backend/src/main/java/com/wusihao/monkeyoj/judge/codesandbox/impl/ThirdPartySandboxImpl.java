package com.wusihao.monkeyoj.judge.codesandbox.impl;

import com.wusihao.monkeyoj.judge.codesandbox.CodeSandbox;
import com.wusihao.monkeyoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyoj.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:00
 * @version: 1.0
 * @description: 第三方代码沙箱，用于调用别人的接口
 */
public class ThirdPartySandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("第三方代码沙箱");
        return null;
    }
}
