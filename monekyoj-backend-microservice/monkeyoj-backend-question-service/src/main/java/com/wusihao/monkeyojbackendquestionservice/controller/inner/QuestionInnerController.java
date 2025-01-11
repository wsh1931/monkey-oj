package com.wusihao.monkeyojbackendquestionservice.controller.inner;

import com.wusihao.monkeyojbackendmodel.model.entity.Question;
import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import com.wusihao.monkeyojbackendquestionservice.service.QuestionService;
import com.wusihao.monkeyojbackendquestionservice.service.QuestionSubmitService;
import com.wusihao.monkeyojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: wusihao
 * @date: 2025/1/11 11:18
 * @version: 1.0
 * @description: 内部调用
 */
@RestController
@RequestMapping("/inner")
public class QuestionInnerController implements QuestionFeignClient {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;

    @GetMapping("/get/id")
    @Override
    public Question getQuestionById(@RequestParam("questionId") long questionId){
        return questionService.getById(questionId);
    }

    @GetMapping("/question_submit/get/id")
    @Override
    public QuestionSubmit getQuestionSubmitById(@RequestParam("questionSubmitId") long questionSubmitId){
        return questionSubmitService.getById(questionSubmitId);
    }

    @PostMapping("/question_submit/update")
    @Override
    public boolean updateQuestionSubmitById(@RequestBody QuestionSubmit questionSubmit){
        return questionSubmitService.updateById(questionSubmit);
    }
}
