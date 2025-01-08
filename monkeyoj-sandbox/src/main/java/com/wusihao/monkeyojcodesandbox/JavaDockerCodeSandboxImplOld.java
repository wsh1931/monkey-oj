package com.wusihao.monkeyojcodesandbox;

import cn.hutool.core.date.StopWatch;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.ArrayUtil;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.command.ExecStartResultCallback;
import com.wusihao.monkeyojcodesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyojcodesandbox.model.ExecuteMessage;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: wusihao
 * @date: 2025/1/3 15:44
 * @version: 1.0
 * @description: Java代码沙箱实现
 */
public class JavaDockerCodeSandboxImplOld extends JavaCodeSandboxTemplate {
//    public static final String GLOBAL_CODE_DIR_NAME = "tempCode";
//    public static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";

    public static final long TIME_OUT = 5000L;

//    public static final String SECURITY_MANAGE_PATH = "D:\\JAVA\\project\\monkey-oj\\monkeyoj-sandbox\\src\\main\\resources\\security";
//
//    public static final String SECURITY_MANAGE_CLASS_NAME = "MySecurityManager";
//
//    public static final List<String> blackList = Arrays.asList("File", "exec");

    public static final Boolean FIRST_INIT = true;

    public static void main(String[] args) {
        ExecuteCodeRequest executeCodeRequest = new ExecuteCodeRequest();
        executeCodeRequest.setInputList(Arrays.asList("1 2", "1 3", "1 5"));
        executeCodeRequest.setLanguage("java");
        String readStr = ResourceUtil.readStr("testCode/simpleComputeArgs/Main.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/simpleCompute/Main.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/SleepError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/MemoryError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/ReadFileError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/WriteFileError.java", StandardCharsets.UTF_8);
//        String readStr = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
        executeCodeRequest.setCode(readStr);

        JavaDockerCodeSandboxImplOld javaNativeCodeSandbox = new JavaDockerCodeSandboxImplOld();
        javaNativeCodeSandbox.executeCode(executeCodeRequest);
    }

    /**
     * 3： 执行代码得到输出结果
     *
     * @param inputList
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/8 17:57
     */
    @Override
    public List<ExecuteMessage> runFile(File userCodeFile, List<String> inputList) {
        // 创建容器，把编译文件传送到容器内
        //获取默认的Docker Client
        String userCodeParentPath = userCodeFile.getParentFile().getAbsolutePath();
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
                .withMemorySwap(0L)
                // 设置容器cpu数目
                .withCpuCount(1L)
//                linux自带安全管理配置
//                .withSecurityOpts(Arrays.asList("seccomp=安全管理配置字符串"))
                // 将文件 userCodePath 挂载到容器的/app目录下
                .setBinds(new Bind(userCodeParentPath, new Volume("/app")));

        // 获取输入，并且得到终端的输出
        CreateContainerResponse createContainerResponse = containerCmd
                // 限制用户不能向根目录写文件
                .withReadonlyRootfs(true)
                // 禁止外部网络的请求
                .withNetworkDisabled(true)
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
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        // 创建执行命令并获取结果 java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s

        for (String inputArgs : inputList) {
            try {
                // 防止内存监督程序监督不到
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String[] inputArgsArray = inputArgs.split(" ");
            String[] cmdArray = ArrayUtil.append(new String[]{"java", "-cp", "/app", "Main"}, inputArgsArray);
            ExecCreateCmdResponse execCreateCmdResponse = dockerClient.execCreateCmd(containerId)
                    .withCmd(cmdArray)
                    .withAttachStderr(true)
                    .withAttachStdin(true)
                    .withAttachStdout(true)
                    .exec();
            System.out.println("创建执行命令：" + execCreateCmdResponse);

            // 返回结果
            ExecuteMessage executeMessage = new ExecuteMessage();
            final String[] message = { null };
            final String[] errorMessage = { null };
            long time = 0L;
            final boolean[] timeout = {true};
            // 定义执行命令返回结果
            String execId = execCreateCmdResponse.getId();
            ExecStartResultCallback execStartResultCallback = new ExecStartResultCallback() {
                @Override
                public void onNext(Frame frame) {
                    StreamType streamType = frame.getStreamType();
                    if (StreamType.STDERR.equals(streamType)) {
                        errorMessage[0] = new String(frame.getPayload());
                        System.err.println("输出结果错误: " + errorMessage[0]);
                    } else {
                        message[0] = new String(frame.getPayload());
                        System.out.println("输出结果正确: " + message[0]);
                    }
                    super.onNext(frame);
                }

                // 若在规定时间内完成则会执行此方法，说明该程序超时
                @Override
                public void onComplete() {
                    timeout[0] = false;
                    super.onComplete();
                }
            };

            final long[] maxMemory = {0};
            // 获取程序占用内存
            StatsCmd statsCmd = dockerClient.statsCmd(containerId);
            ResultCallback<Statistics> statisticsResultCallback = new ResultCallback<Statistics>() {
                @Override
                public void onNext(Statistics statistics) {
                    Long maxUsage = statistics.getMemoryStats().getMaxUsage();
                    maxMemory[0] = Math.max(maxMemory[0], maxUsage);
                    System.out.println("最大内存占用："+ maxMemory[0]);
                }

                @Override
                public void onStart(Closeable closeable) {

                }

                @Override
                public void onError(Throwable throwable) {

                }

                @Override
                public void onComplete() {

                }

                @Override
                public void close() throws IOException {

                }
            };
            try {
                // 执行命令
                StopWatch stopWatch = new StopWatch();
                statsCmd.exec(statisticsResultCallback);
                stopWatch.start();
                dockerClient.execStartCmd(execId)
                        .exec(execStartResultCallback)
                        // 超时后会继续执行，不会执行 onComplete 方法
                        .awaitCompletion(TIME_OUT, TimeUnit.MILLISECONDS);
                stopWatch.stop();
                statsCmd.close();
                time = stopWatch.getLastTaskTimeMillis();
                executeMessage.setTime(time);
            } catch (InterruptedException e) {
                System.err.println("程序执行异常");
                throw new RuntimeException(e);
            }

            executeMessage.setMessage(message[0]);
            executeMessage.setErrorMessage(errorMessage[0]);

            executeMessage.setMemory(maxMemory[0]);
            executeMessageList.add(executeMessage);
        }

        return executeMessageList;
    }
}
