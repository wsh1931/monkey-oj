package com.wusihao.monkeyojjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.wusihao.monkeyojjudgeservice.judge.codesandbox.CodeSandbox;
import org.apache.commons.lang3.StringUtils;
import wusihao.monkeyojbackendcommon.common.ErrorCode;
import wusihao.monkeyojbackendcommon.exception.BusinessException;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:00
 * @version: 1.0
 * @description: 远程代码沙箱： 实际调用接口的沙箱
 */
public class RemoteSandboxImpl implements CodeSandbox {
    // 定义鉴权请求头和密钥
    public static final String AUTH_REQUEST_HEADER = "auth";

    public static final String AUTH_REQUEST_SECRET = "secretKey";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://localhost:8122/java/executeCode/native";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTH_REQUEST_HEADER, AUTH_REQUEST_SECRET)
                .body(json)
                .execute()
                .body();

        System.out.println("responseStr: = " + responseStr);
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "执行远程沙箱异常: " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
