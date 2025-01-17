package com.wusihao.monkeyojjudgeservice.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.JudgeInfo;
import com.wusihao.monkeyojbackendmodel.model.dto.question.JudgeCase;
import com.wusihao.monkeyojbackendmodel.model.dto.question.JudgeConfig;
import com.wusihao.monkeyojbackendmodel.model.entity.Question;
import com.wusihao.monkeyojbackendmodel.model.enums.JudgeInfoMessageEnum;

import java.util.List;
import java.util.Optional;

/**
 * @author: wusihao
 * @date: 2025/1/2 17:31
 * @version: 1.0
 * @description: java判题策略
 */
public class JavaJudgeStrategy implements JudgeStrategy {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/2 17:42
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        // 根据题目结果判断执行是否正确
        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        Long userLimitTime = Optional.ofNullable(judgeInfo.getTime()).orElse(0L);
        Long userLimitMemory = Optional.ofNullable(judgeInfo.getMemory()).orElse(0L);
        JudgeInfo judgeInfoResponse = new JudgeInfo();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.ACCEPT;
        judgeInfoResponse.setTime(userLimitTime);
        judgeInfoResponse.setMemory(userLimitMemory);

        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        Question question = judgeContext.getQuestion();

        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        if (inputList.size() != outputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        // 判断输入与输出是否相等
        for (int i = 0; i < judgeCaseList.size(); i++) {
            if (!judgeCaseList.get(i).getOutput().equals(outputList.get(i))) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WRONG_ANSWER;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }

        // 判断题目限制
        String judgeConfigStr = question.getJudgeConfig();
        JudgeConfig judgeConfig = JSONUtil.toBean(judgeConfigStr, JudgeConfig.class);
        Long systemTimeLimit = judgeConfig.getTimeLimit();
        Long systemMemoryLimit = judgeConfig.getMemoryLimit();

        if (userLimitMemory > systemMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        // Java执行慢，需要额外的执行时间
        long JAVA_TIME_COST = 10000L;
        if (userLimitTime - JAVA_TIME_COST > systemTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());

        return judgeInfoResponse;
    }
}
