package com.wusihao.monkeyojjudgeservice.judge;

import com.wusihao.monkeyojbackendmodel.model.codesandbox.JudgeInfo;
import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import com.wusihao.monkeyojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.wusihao.monkeyojjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.wusihao.monkeyojjudgeservice.judge.strategy.JavaJudgeStrategy;
import com.wusihao.monkeyojjudgeservice.judge.strategy.JudgeContext;
import com.wusihao.monkeyojjudgeservice.judge.strategy.JudgeStrategy;
import org.springframework.stereotype.Service;

/**
 * @author: wusihao
 * @date: 2025/1/2 18:14
 * @version: 1.0
 * @description: 判题管理（简化调用）
 */
@Service
public class JudgeManager {
    /**
     * 执行判题选择语言
     *
     * @param judgeContext
     * @return {@link null}
     * @author wusihao
     * @date 2025/1/2 17:31
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        if (QuestionSubmitLanguageEnum.JAVA.getValue().equals(language)) {
            judgeStrategy = new JavaJudgeStrategy();
        }

        return judgeStrategy.doJudge(judgeContext);
    }
}
