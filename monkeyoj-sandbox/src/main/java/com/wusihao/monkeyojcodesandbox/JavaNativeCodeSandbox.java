package com.wusihao.monkeyojcodesandbox;

import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeResponse;

/**
 * @author: wusihao
 * @date: 2025/1/8 17:49
 * @version: 1.0
 * @description: Java代码沙箱原生实现，直接复用模板方法
 */
public class JavaNativeCodeSandbox extends JavaCodeSandboxTemplate {
    /**
     * 若重写则调用重写的方法，若不重写则默认调用JavaCodeSandboxTemplate方法
     *
     * @param executeCodeRequest
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 17:51
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        return super.executeCode(executeCodeRequest);
    }
}
