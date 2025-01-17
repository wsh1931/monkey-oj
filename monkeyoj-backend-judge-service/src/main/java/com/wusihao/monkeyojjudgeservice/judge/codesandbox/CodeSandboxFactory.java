package com.wusihao.monkeyojjudgeservice.judge.codesandbox;


import com.wusihao.monkeyojjudgeservice.judge.codesandbox.impl.ExampleSandboxImpl;
import com.wusihao.monkeyojjudgeservice.judge.codesandbox.impl.RemoteSandboxImpl;
import com.wusihao.monkeyojjudgeservice.judge.codesandbox.impl.ThirdPartySandboxImpl;

/**
 * @author: wusihao
 * @date: 2025/1/2 14:33
 * @version: 1.0
 * @description: 代码沙箱创建工厂（根据字符串创建指定代码沙箱实例）
 */
public class CodeSandboxFactory {
    /**
     * 创建代码沙箱实例
     *
     * @param type 代码沙箱类型
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/2 14:35
     */
    public static CodeSandbox newInstance(String type) {
        switch (type) {
            case "example":
                return new ExampleSandboxImpl();
            case "remote":
                return new RemoteSandboxImpl();
            case "thirdParty":
                return new ThirdPartySandboxImpl();
            default:
                return new ExampleSandboxImpl();
        }
    }
}
