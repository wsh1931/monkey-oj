package com.wusihao.monkeyojcodesandbox.controller;

import com.wusihao.monkeyojcodesandbox.JavaDockerCodeSandboxImpl;
import com.wusihao.monkeyojcodesandbox.JavaNativeCodeSandboxImpl;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author: wusihao
 * @date: 2025/1/3 11:25
 * @version: 1.0
 * @description:
 */
@RestController
@RequestMapping("/java")
public class JavaCodeExecuteController {

    // 定义鉴权请求头和密钥, 防止第三方直接调用沙箱接口，造成资源损失
    public static final String AUTH_REQUEST_HEADER = "auth";

    public static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource
    private JavaNativeCodeSandboxImpl javaNativeCodeSandboxImpl;

    @Resource
    private JavaDockerCodeSandboxImpl javaDockerCodeSandbox;
    @PostMapping("/executeCode/native")
    ExecuteCodeResponse JavaExecuteCodeNative(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        // 基本的认证
        String authRequestHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authRequestHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandboxImpl.executeCode(executeCodeRequest);
        return executeCodeResponse;
    }

    @PostMapping("/executeCode/docker")
    ExecuteCodeResponse JavaExecuteCodeDocker(@RequestBody ExecuteCodeRequest executeCodeRequest,
                                              HttpServletRequest request,
                                              HttpServletResponse response) {
        // 基本的认证
        String authRequestHeader = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(authRequestHeader)) {
            response.setStatus(403);
            return null;
        }
        if (executeCodeRequest == null) {
            throw new RuntimeException("请求参数为空");
        }
        ExecuteCodeResponse executeCodeResponse = javaDockerCodeSandbox.executeCode(executeCodeRequest);
        return executeCodeResponse;
    }

    @GetMapping("/health")
    public String healthCheck() {
        return "测试";
    }
}
