package com.wusihao.monkeyoj.judge;

import com.wusihao.monkeyoj.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
