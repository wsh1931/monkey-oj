package com.wusihao.monkeyoj.job.judge;

import com.wusihao.monkeyoj.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
