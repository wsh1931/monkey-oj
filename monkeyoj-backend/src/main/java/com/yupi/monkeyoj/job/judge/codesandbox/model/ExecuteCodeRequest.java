package com.yupi.monkeyoj.job.judge.codesandbox.model;

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
public class ExecuteCodeRequest {
    private List<String> inputList;
    private String language;
    private String code;
}
