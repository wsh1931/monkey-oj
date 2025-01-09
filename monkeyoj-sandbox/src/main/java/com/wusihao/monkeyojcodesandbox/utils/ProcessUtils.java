package com.wusihao.monkeyojcodesandbox.utils;

import cn.hutool.core.util.StrUtil;
import com.wusihao.monkeyojcodesandbox.model.ExecuteMessage;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
            // 等待程序执行完成，获得错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            if (exitValue == 0) {
                System.out.println(opName + "成功");
                // 得到控制台的打印信息
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String> outputStrList = new ArrayList<>();
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null) {
                    outputStrList.add(compileOutputLine);
                }

                executeMessage.setMessage(StringUtils.join(outputStrList, "\n"));
            } else {
                //异常退出
                System.out.println(opName+"失败，错误码" + exitValue);
                //分批获取进程的正常输出
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                List<String>outputList=new ArrayList<>();
                //逐行读取
                String compileOutputLine;
                while((compileOutputLine= bufferedReader.readLine()) != null){
                    outputList.add(compileOutputLine);
                }
                executeMessage.setMessage(StringUtils.join(outputList,"\n"));
                //分批获取进程的错误输出
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));

                //逐行读取
                List<String>errorOutputList = new ArrayList<>();
                //逐行读取
                String errorCompileOutputLine;
                while((errorCompileOutputLine= errorBufferedReader.readLine()) != null){
                    errorOutputList.add(errorCompileOutputLine);
                }
                executeMessage.setErrorMessage(StringUtils.join(errorOutputList,"\n"));
            }
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
