package com.wusihao.monkeyojbackendquestionservice.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wusihao.monkeyojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wusihao.monkeyojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import com.wusihao.monkeyojbackendmodel.model.entity.User;
import com.wusihao.monkeyojbackendmodel.model.vo.QuestionSubmitVO;

/**
* @author 吴思豪
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-12-28 18:38:36
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmit, User loginUser);

}
