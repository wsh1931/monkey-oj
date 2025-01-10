package com.wusihao.monkeyojbackendserviceclient.service;


import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;

public interface JudgeService {
    QuestionSubmit doJudge(long questionSubmitId);
}
