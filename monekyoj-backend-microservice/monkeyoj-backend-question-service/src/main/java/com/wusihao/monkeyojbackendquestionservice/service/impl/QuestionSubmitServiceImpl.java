package com.wusihao.monkeyojbackendquestionservice.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wusihao.monkeyojbackendmodel.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.wusihao.monkeyojbackendmodel.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.wusihao.monkeyojbackendmodel.model.entity.Question;
import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import com.wusihao.monkeyojbackendmodel.model.entity.User;
import com.wusihao.monkeyojbackendmodel.model.enums.QuestionSubmitLanguageEnum;
import com.wusihao.monkeyojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.wusihao.monkeyojbackendmodel.model.vo.QuestionSubmitVO;
import com.wusihao.monkeyojbackendquestionservice.mapper.QuestionSubmitMapper;
import com.wusihao.monkeyojbackendquestionservice.rabbitmq.EventConstant;
import com.wusihao.monkeyojbackendquestionservice.rabbitmq.MessageProducerMethods;
import com.wusihao.monkeyojbackendquestionservice.rabbitmq.RabbitmqExchangeName;
import com.wusihao.monkeyojbackendquestionservice.rabbitmq.RabbitmqRoutingName;
import com.wusihao.monkeyojbackendquestionservice.service.QuestionService;
import com.wusihao.monkeyojbackendquestionservice.service.QuestionSubmitService;
import com.wusihao.monkeyojbackendserviceclient.service.JudgeFeignClient;
import com.wusihao.monkeyojbackendserviceclient.service.UserFeignClient;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import wusihao.monkeyojbackendcommon.common.ErrorCode;
import wusihao.monkeyojbackendcommon.constant.CommonConstant;
import wusihao.monkeyojbackendcommon.exception.BusinessException;
import wusihao.monkeyojbackendcommon.utils.SqlUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 吴思豪
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-12-28 18:38:36
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService {
    @Resource
    private QuestionService questionService;
    @Resource
    private UserFeignClient userFeignClient;

    @Resource
    @Lazy
    private JudgeFeignClient judgeFeignClient;
    @Resource
    private MessageProducerMethods messageProducerMethods;

    /**
     * 用户题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录id
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言是否合法
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum enumByValue = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (enumByValue == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言错误");
        }
        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        // 是否已题目提交
        long userId = loginUser.getId();
        // 每个用户串行题目提交
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);

        // 设置初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");
        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "数据插入失败");
        }

        // 执行判题服务
        Long questionSubmitId = questionSubmit.getId();
        JSONObject data = new JSONObject();
        data.putOnce("questionSubmitId", questionSubmitId);
        data.putOnce("event", EventConstant.javaNativeCodeExecute);
        messageProducerMethods.sendMessage(RabbitmqExchangeName.executeCodeExchange,
                RabbitmqRoutingName.javaNativeCodeExecuteRouting,
                data.toString());
        return questionSubmitId;
    }

    /**
     * 获取查询包装类（用户根据字段查询）
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        // 拼接查询条件
        queryWrapper.eq(StringUtils.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(questionId), "questionId", questionId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status)!=null, "status", status);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        //仅本人和管理员能看见自己的答案
        Long userId = loginUser.getId();
        //处理脱敏
        if(!Objects.equals(userId, questionSubmit.getUserId()) &&!userFeignClient.isAdmin(loginUser)){
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVOPage;
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = questionSubmitList.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userFeignClient.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        // 填充信息
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream().map(questionSubmit -> {
            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
            Long userId = questionSubmit.getUserId();
            User user = null;
            if (userIdUserListMap.containsKey(userId)) {
                user = userIdUserListMap.get(userId).get(0);
            }
            // 非本人或管理员看不到代码
            if(!Objects.equals(userId, loginUser.getId()) &&!userFeignClient.isAdmin(loginUser)){
                questionSubmitVO.setCode(null);
            }
            questionSubmitVO.setUserVO(userFeignClient.getUserVO(user));
            return questionSubmitVO;
        }).collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




