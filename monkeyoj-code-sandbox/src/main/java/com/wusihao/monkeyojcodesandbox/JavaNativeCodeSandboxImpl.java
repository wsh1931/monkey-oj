package com.wusihao.monkeyojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeResponse;
import com.wusihao.monkeyojcodesandbox.model.ExecuteMessage;
import com.wusihao.monkeyojcodesandbox.model.JudgeInfo;
import com.wusihao.monkeyojcodesandbox.utils.ProcessUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author: wusihao
 * @date: 2025/1/3 15:44
 * @version: 1.0
 * @description:
 */
public class JavaNativeCodeSandboxImpl implements CodeSandbox {
    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        executeCodeRequest.setLanguage("java");
        String readStr = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/simpleCompute/Main.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(readStr);

        JavaNativeCodeSandboxImpl javaNativeCodeSandbox = new JavaNativeCodeSandboxImpl();
        javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();
        // 得到当前项目更目录
        String userDir = System.getProperty("user.dir");
        // 得到tempCode路径，使用File.separator分隔开是因为在linux/windows中的路径分隔符不一样
        String globalCodePathName =userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局父目录是否存在
        if (!FileUtil.exist(globalCodePathName)) {
            // 若不存在，则建立此目录
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        // 将code写入userCodePath文件中
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);


        // 编译代码，得到 class 文件
        String compiledCmd = String.format("javac -encoding utf-8 %s",userCodeFile.getAbsoluteFile());
        try {
            // 编译代码
            Process compileProcess = Runtime.getRuntime().exec(compiledCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }


        // 执行代码
        List<String> inputList = executeCodeRequest.getInputList();
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs: inputList) {
            String runCmd = String.format("java -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
//                ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, "运行", inputArgs);
                executeMessageList.add(executeMessage);
                System.out.println(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }

        }

        // 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        List<String> outPutList = new ArrayList<>();
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList) {
            String errorMessage = executeMessage.getErrorMessage();
            if (StrUtil.isNotBlank(errorMessage)) {
                // 执行结果存在错误
                executeCodeResponse.setMessage(errorMessage);
                executeCodeResponse.setStatus(3);
                break;
            }
            outPutList.add(executeMessage.getMessage());
            Long executeMessageTime = executeMessage.getTime();
            // 得到运行最大时间
            if (executeMessageTime != null) {
                maxTime = Math.max(maxTime, executeMessageTime);
            }
        }

        if (outPutList.size() == executeMessageList.size()) {
            executeCodeResponse.setStatus(1);
        }

        executeCodeResponse.setOutputList(outPutList);
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        executeCodeResponse.setJudgeInfo(judgeInfo);

        // 执行完代码后，文件清理
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功": "失败"));
        }
        return executeCodeResponse;
    }

    /**
     * 获取错误响应
     *
     * @param e
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/4 11:06
     */
    private ExecuteCodeResponse getErrorResponse(Exception e) {
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(new ArrayList<>());
        executeCodeResponse.setMessage(e.getMessage());
        // 代码沙箱错误
        executeCodeResponse.setStatus(2);
        executeCodeResponse.setJudgeInfo(new JudgeInfo());
        return executeCodeResponse;
    }

}
