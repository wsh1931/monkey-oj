package com.wusihao.monkeyojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeResponse;
import com.wusihao.monkeyojcodesandbox.model.ExecuteMessage;
import com.wusihao.monkeyojcodesandbox.model.JudgeInfo;
import com.wusihao.monkeyojcodesandbox.utils.ProcessUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StopWatch;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author: wusihao
 * @date: 2025/1/8 16:42
 * @version: 1.0
 * @description:
 */
@Slf4j
public abstract class JavaCodeSandboxTemplate implements CodeSandbox {
    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static final long TIME_OUT = 5000L;

//    public static final String SECURITY_MANAGE_PATH = "D:\\JAVA\\project\\monkey-oj\\monkeyoj-sandbox\\src\\main\\resources\\security";
//
//    public static final String SECURITY_MANAGE_CLASS_NAME = "MySecurityManager";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        //        System.setSecurityManager(new DefaultSecurityManager());
//        System.setSecurityManager(new DenySecurityManager());
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();

        //  校验代码中是否包含黑名单中的禁用词
//        FoundWord foundWord = WORD_TREE.matchWord(code);
//        if (foundWord != null) {
//            System.out.println("包含禁止词：" + foundWord.getFoundWord());
//            return null;
//        }
        // 1: 把用户代码保存为文件
        File userCodeFile = saveCodeToFile(code);

        // 2: 编译代码，得到 class 文件
        ExecuteMessage compileFileExecuteMessage = compileFile(userCodeFile);
        System.out.println(compileFileExecuteMessage);

        // 3： 执行代码得到输出结果
        List<String> inputList = executeCodeRequest.getInputList();
        List<ExecuteMessage> executeMessageList = runFile(userCodeFile, inputList);
        // 4: 收集整理输出结果
        ExecuteCodeResponse executeCodeResponse = getOutPutResponse(executeMessageList);

        // 5: 执行完代码后，文件清理
        boolean deleteFile = deleteFile(userCodeFile);
        if (!deleteFile) {
            log.error("删除文件失败, userCodeFilePath = " + userCodeFile.getAbsoluteFile());
        }
        return executeCodeResponse;
    }

    /**
     * 把用户代码保存为文件
     *
     * @param code 用户代码
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 16:47
     */
    public File saveCodeToFile(String code) {
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

        return userCodeFile;
    }

    /**
     * 编译代码，得到 class 文件
     *
     * @param userCodeFile
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 16:52
     */
    public ExecuteMessage compileFile(File userCodeFile) {
        String compiledCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            // 编译代码
            Process compileProcess = Runtime.getRuntime().exec(compiledCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            if (executeMessage.getExitValue() != 0) {
                throw new RuntimeException("编译错误");
            }
            return executeMessage;
        } catch (Exception e) {
            throw new RuntimeException(e);
//            return getErrorResponse(e);
        }
    }

    /**
     * 执行文件，获得返回结果
     *
     * @param inputList 输入列表
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 17:17
     */
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        // 得到当前项目根目录
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();

        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs: inputList) {
            // java -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=MySecurityManager Main
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            // 不推荐在Java9中实现
//            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s;%s -Djava.security.manager=%s Main %s", userCodeParentPath, SECURITY_MANAGE_PATH, SECURITY_MANAGE_CLASS_NAME, inputArgs);
            try {
                // 计算时间
                StopWatch stopWatch = new StopWatch();
                stopWatch.start();
                // 开启新的子线程执行
                Process runProcess = Runtime.getRuntime().exec(runCmd);
                // 定时，超过时间后杀死进程，防止用户写无限循环的程序占用资源
                new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        System.err.println("超时了，中断");
                        runProcess.destroy();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
//                ExecuteMessage executeMessage = ProcessUtils.runInteractProcessAndGetMessage(runProcess, "运行", inputArgs);
                // 得到开始到结束的执行时间
                stopWatch.stop();
                executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
                executeMessageList.add(executeMessage);
                System.out.println(executeMessage);
            } catch (Exception e) {
                throw new RuntimeException("程序执行异常", e);
            }

        }

        return executeMessageList;
    }

    /**
     * 收集整理输出结果
     *
     * @param executeMessageList 等待整理的结果列表
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 17:20
     */
    public ExecuteCodeResponse getOutPutResponse(List<ExecuteMessage> executeMessageList) {
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

        return executeCodeResponse;
    }

    /**
     * 删除文件
     *
     * @param userCodeFile
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 17:33
     */
    public boolean deleteFile(File userCodeFile) {
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功": "失败"));

            return del;
        }
        return true;
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
