package com.wusihao.monkeyojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.wusihao.monkeyojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.*;

/**
 * @author: wusihao
 * @date: 2025/1/3 17:42
 * @version: 1.0
 * @description: 进程工具类
 */
public class ProcessUtils {
    /**
     * 执行进程并获取信息
     *
     * @param runProcess
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/3 17:51
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待程序执行完成，获得错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 得到控制台的打印信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    compileOutputStringBuilder.append(compileOutputLine.toString());
                }
                executeMessage.setMessage(compileOutputStringBuilder.toString());
                System.out.println(compileOutputStringBuilder);
            } else {
                System.out.println(opName + "失败, 错误码: " + exitValue);
                // 得到控制台的错误流信息
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null) {
                    errorCompileOutputStringBuilder.append(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(errorCompileOutputStringBuilder.toString());
                System.err.println(errorCompileOutputStringBuilder);
            }
            // 得到开始到结束的执行时间
            stopWatch.stop();
            executeMessage.setTime(stopWatch.getLastTaskTimeMillis());
            stopWatch.getLastTaskTimeMillis();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return executeMessage;
    }

    /**
     * 执行交互式进程并获取信息
     *
     * @param runProcess
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/3 17:51
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try {
            OutputStream outputStream = runProcess.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);

            String[] split = args.split(" ");
            // 向控制台输入信息
            outputStreamWriter.write(StrUtil.join("\n", split) + "\n");
            // 相当于回车，确认写
            outputStreamWriter.flush();

            // 得到控制台的打印信息
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null) {
                compileOutputStringBuilder.append(compileOutputLine);
            }
            executeMessage.setMessage(compileOutputStringBuilder.toString());

            // 记得资源的释放，否在会卡死
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            runProcess.destroy();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return executeMessage;
    }
}
