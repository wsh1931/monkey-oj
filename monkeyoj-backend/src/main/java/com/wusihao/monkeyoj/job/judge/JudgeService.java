package com.wusihao.monkeyoj.job.judge;

import com.wusihao.monkeyoj.model.vo.QuestionSubmitVO;

public interface JudgeService {
    QuestionSubmitVO doJudge(long questionSubmitId);
}
