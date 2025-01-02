package com.wusihao.monkeyoj.job.judge.codesandbox.impl;

import com.wusihao.monkeyoj.job.judge.codesandbox.CodeSandbox;
import com.wusihao.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;
import com.wusihao.monkeyoj.model.dto.questionsubmit.JudgeInfo;
import com.wusihao.monkeyoj.model.enums.JudgeInfoMessageEnum;
import com.wusihao.monkeyoj.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:00
 * @version: 1.0
 * @description: 示例代码沙箱
 */
public class ExampleSandboxImpl implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();;

        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPT.getText());
        judgeInfo.setTime(1000L);
        judgeInfo.setMemory(1000L);

        executeCodeResponse.setJudgeInfo(judgeInfo);

        System.out.println("示例代码沙箱");
        return executeCodeResponse;
    }
}
