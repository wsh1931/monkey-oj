package com.wusihao.monkeyojjudgeservice.judge;


import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
