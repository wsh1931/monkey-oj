package com.wusihao.monkeyojjudgeservice.judge.codesandbox;

import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: wusihao
 * @date: 2025/1/2 15:29
 * @version: 1.0
 * @description:
 */
@Slf4j
public class CodeSandboxProxy implements CodeSandbox {

    private final CodeSandbox codeSandbox;

    public CodeSandboxProxy(CodeSandbox codeSandbox) {
        this.codeSandbox = codeSandbox;
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求信息: " + executeCodeRequest.toString());
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        log.info("代码沙箱响应信息: " + executeCodeResponse.toString());
        return executeCodeResponse;
    }
}
