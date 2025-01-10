package com.wusihao.monkeyojjudgeservice.judge.strategy;

import com.wusihao.monkeyojbackendmodel.model.codesandbox.JudgeInfo;
import com.wusihao.monkeyojbackendmodel.model.dto.question.JudgeCase;
import com.wusihao.monkeyojbackendmodel.model.entity.Question;
import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/2 17:30
 * @version: 1.0
 * @description: 用于定义在策略中传递的参数
 */
@Data
public class JudgeContext {
    private JudgeInfo judgeInfo;
    private List<String> inputList;
    private List<String> outputList;
    private List<JudgeCase>judgeCaseList;
    private Question question;
    private QuestionSubmit questionSubmit;
}
