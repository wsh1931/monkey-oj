package com.wusihao.monkeyojjudgeservice.controller.inner;

import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import com.wusihao.monkeyojbackendserviceclient.service.JudgeFeignClient;
import com.wusihao.monkeyojjudgeservice.judge.JudgeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: wusihao
 * @date: 2025/1/11 11:19
 * @version: 1.0
 * @description: 内部调用
 */
@RestController
@RequestMapping("/inner")
public class JudgeInnerController implements JudgeFeignClient {
    @Resource
    private JudgeService judgeService;
    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    @Override
    @PostMapping("/do")
    public QuestionSubmit doJudge(@RequestParam("questionSubmitId") long questionSubmitId){
        return judgeService.doJudge(questionSubmitId);
    }
}
