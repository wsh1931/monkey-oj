package com.wusihao.monkeyoj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wusihao.monkeyoj.common.BaseResponse;
import com.wusihao.monkeyoj.common.ErrorCode;
import com.wusihao.monkeyoj.common.ResultUtils;
import com.wusihao.monkeyoj.exception.BusinessException;
import com.wusihao.monkeyoj.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wusihao.monkeyoj.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wusihao.monkeyoj.model.entity.QuestionSubmit;
import com.wusihao.monkeyoj.model.entity.User;
import com.wusihao.monkeyoj.model.vo.QuestionSubmitVO;
import com.wusihao.monkeyoj.service.QuestionSubmitService;
import com.wusihao.monkeyoj.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 *
 * @author <a href="https://github.com/wsh1931">吴思豪</a>
 *
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return 提价记录id
     */
    @PostMapping("/")
    public BaseResponse<Long> doSubmitQuestion(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
            HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能提交
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表：除了管理员外，用户只能看到非答案，提交代码等公开信息
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return 提价记录id
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        //从数据提取到原始的分页信息
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        final User loginUser=userService.getLoginUser(request);
        //返回脱敏信息
        return ResultUtils.success(questionSubmitService.getQuestionSubmitVOPage(questionSubmitPage,loginUser));
    }
}
