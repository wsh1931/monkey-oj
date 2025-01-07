package com.wusihao.monkeyojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
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
public class JavaDockerCodeSandboxImpl implements CodeSandbox {
    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static final long TIME_OUT = 5000L;

    public static final String SECURITY_MANAGE_PATH = "D:\\JAVA\\project\\monkey-oj\\monkeyoj-sandbox\\src\\main\\resources\\security";

    public static final String SECURITY_MANAGE_CLASS_NAME = "MySecurityManager";

    public static final List<String> blackList = Arrays.asList("File", "exec");

    public static final Boolean FIRST_INIT = true;

    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3"));
        executeCodeRequest.setLanguage("java");
        String readStr = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/simpleCompute/Main.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/SleepError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/MemoryError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/ReadFileError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/WriteFileError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(readStr);

        JavaDockerCodeSandboxImpl javaNativeCodeSandbox = new JavaDockerCodeSandboxImpl();
        javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
//        System.setSecurityManager(new DefaultSecurityManager());
//        System.setSecurityManager(new DenySecurityManager());
        String language = executeCodeRequest.getLanguage();
        String code = executeCodeRequest.getCode();
        List<String> inputList = executeCodeRequest.getInputList();
        // 得到当前项目更目录
        String userDir = System.getProperty("user.dir");
        // 得到tempCode路径，使用File.separator分隔开是因为在linux/windows中的路径分隔符不一样
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
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
        String compiledCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsoluteFile());
        try {
            // 编译代码
            Process compileProcess = Runtime.getRuntime().exec(compiledCmd);
            ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(executeMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 创建容器，吧编译文件传送到容器内
        //获取默认的Docker Client
        DockerClient dockerClient = DockerClientBuilder.getInstance().build();
        //        PingCmd pingCmd=dockerClient.pingCmd();
        //        pingCmd.exec();
        //拉取镜像
        String image = "openjdk:8-alpine";
        if (FIRST_INIT) {
            PullImageCmd pullImageCmd = dockerClient.pullImageCmd(image);
            PullImageResultCallback pullImageResultCallback = new PullImageResultCallback() {
                @Override
                public void onNext(PullResponseItem item) {
                    System.out.println("下载镜像: " + item.getStatus());
                    super.onNext(item);
                }
            };
            try {
                pullImageCmd.exec(pullImageResultCallback).awaitCompletion();
            } catch (InterruptedException e) {
                System.out.println("拉取镜像异常");
                throw new RuntimeException(e);
            }
        }

        System.out.println("下载完成");

//        创建容器
        CreateContainerCmd containerCmd = dockerClient.createContainerCmd(image);
        // 将编译后的文件写到容器内
        HostConfig hostConfig = new HostConfig();
        hostConfig
                // 设置容器内存
                .withMemory(100 * 1000 * 1000L)
                // 设置容器cpu数目
                .withCpuCount(1L)
                // 将文件 userCodePath 挂载到容器的/app目录下
                .setBinds(new Bind(userCodeParentPath, new Volume("/app")));

        // 获取输入，并且得到终端的输出
        CreateContainerResponse createContainerResponse = containerCmd
                .withAttachStdin(true)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .withTty(true)
                .withHostConfig(hostConfig)
                .exec();
        System.out.println(createContainerResponse);
        String containerId = createContainerResponse.getId();

//        //启动容器
        dockerClient.startContainerCmd(containerId).exec();
        //执行命令并获取结果
        ArrayList<ExecuteMessage> executeMessageList = new ArrayList<>();
        // 创建执行命令
        for (String inputArgs : inputList) {
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);

            // 执行命令
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        System.err.println("输出结果错误: " + new String(frame.getPayload()));
                    } else {
                        System.out.println("输出结果正确: " + new String(frame.getPayload()));
                    }
                    super.onNext(frame);
                }
            };

            try {
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        .awaitCompletion();
            } catch (InterruptedException e) {
                System.err.println("程序执行异常");
                throw new RuntimeException(e);
            }
        }


        return new ExecuteCodeResponse();
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
