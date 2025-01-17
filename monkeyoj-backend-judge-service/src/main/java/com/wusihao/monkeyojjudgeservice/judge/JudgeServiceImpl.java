package com.wusihao.monkeyojjudgeservice.judge;

import cn.hutool.json.JSONUtil;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeResponse;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.JudgeInfo;
import com.wusihao.monkeyojbackendmodel.model.dto.question.JudgeCase;
import com.wusihao.monkeyojbackendmodel.model.entity.Question;
import com.wusihao.monkeyojbackendmodel.model.entity.QuestionSubmit;
import com.wusihao.monkeyojbackendmodel.model.enums.QuestionSubmitStatusEnum;
import com.wusihao.monkeyojbackendserviceclient.service.QuestionFeignClient;
import com.wusihao.monkeyojjudgeservice.judge.codesandbox.CodeSandbox;
import com.wusihao.monkeyojjudgeservice.judge.codesandbox.CodeSandboxFactory;
import com.wusihao.monkeyojjudgeservice.judge.codesandbox.CodeSandboxProxy;
import com.wusihao.monkeyojjudgeservice.judge.strategy.JudgeContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wusihao.monkeyojbackendcommon.common.ErrorCode;
import wusihao.monkeyojbackendcommon.exception.BusinessException;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: wusihao
 * @date: 2025/1/2 16:22
 * @version: 1.0
 * @description: 判题服务
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;

    @Value("${codesandbox.type:example}")
    private String type;

    @Resource
    private JudgeManager judgeManager;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {
        // 根据题目提交id得到提交信息，题目信息
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        Integer status = questionSubmit.getStatus();
        // 若题目状态不为等待中
        if (!status.equals(QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中，请勿重复操作");
        }

        // 更新题目状态为判题中, 防止代码重复执行
        Long id = questionSubmit.getId();
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.RUNNING.getValue());
        questionSubmitUpdate.setId(id);
        boolean updateById = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!updateById) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 调用代码沙箱，得到执行结果
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        CodeSandboxProxy codeSandboxProxy = new CodeSandboxProxy(codeSandbox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();

        // 获取输入用例
        String judgeCase = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCase, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandboxProxy.executeCode(executeCodeRequest);
        List<String> outputList = executeCodeResponse.getOutputList();
        JudgeInfo judgeInfo = executeCodeResponse.getJudgeInfo();

        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setJudgeInfo(judgeInfo);
        judgeContext.setInputList(inputList);
        judgeContext.setOutputList(outputList);
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);

        // 使用策略模式: 根据题目结果判断执行是否正确
        JudgeInfo doJudge = judgeManager.doJudge(judgeContext);

        questionSubmitUpdate.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());
        questionSubmitUpdate.setId(id);
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(doJudge));
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        QuestionSubmit submit = questionFeignClient.getQuestionSubmitById(questionId);
        return submit;
    }
}
