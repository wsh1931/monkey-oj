package com.wusihao.monkeyojbackendmodel.model.codesandbox;

import lombok.Data;

/**
 * @author: wusihao
 * @date: 2024/12/29 10:18
 * @version: 1.0
 * @description: 题目提交信息
 */
@Data
public class JudgeInfo {
    // 程序执行信息
    private String message;
    // 消耗时间
    private Long time;
    // 消耗内存
    private Long memory;
}
