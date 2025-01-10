package com.wusihao.monkeyoj.judge.strategy;

import com.wusihao.monkeyoj.judge.codesandbox.model.JudgeInfo;

/**
 * @author: wusihao
 * @date: 2025/1/2 17:29
 * @version: 1.0
 * @description: 判题策略
 */
public interface JudgeStrategy {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/2 17:31
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
