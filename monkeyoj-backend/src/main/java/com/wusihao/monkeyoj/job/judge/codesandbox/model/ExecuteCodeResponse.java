package com.wusihao.monkeyoj.job.judge.codesandbox.model;

import com.wusihao.monkeyoj.model.dto.questionsubmit.JudgeInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/2 10:53
 * @version: 1.0
 * @description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeResponse {
    private List<String> ouputList;
    /**
     * 接口信息
     */
    private String message;
    /**
     * 执行状态
     */
    private Integer status;
    private JudgeInfo judgeInfo;
}
