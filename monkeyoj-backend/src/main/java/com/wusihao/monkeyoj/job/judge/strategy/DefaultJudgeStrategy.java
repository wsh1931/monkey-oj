package com.wusihao.monkeyoj.job.judge.strategy;

import cn.hutool.json.JSONUtil;
import com.wusihao.monkeyoj.model.dto.question.JudgeCase;
import com.wusihao.monkeyoj.model.dto.question.JudgeConfig;
import com.wusihao.monkeyoj.model.dto.questionsubmit.JudgeInfo;
import com.wusihao.monkeyoj.model.entity.Question;
import com.wusihao.monkeyoj.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/2 17:31
 * @version: 1.0
 * @description: 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
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
        Long userLimitTime = judgeInfo.getTime();
        Long userLimitMemory = judgeInfo.getMemory();
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

        if (userLimitTime > systemTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.TIME_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());

        return judgeInfoResponse;
    }
}
