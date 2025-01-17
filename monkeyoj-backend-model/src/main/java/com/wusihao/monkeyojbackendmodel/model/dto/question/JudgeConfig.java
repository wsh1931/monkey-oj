package com.wusihao.monkeyojbackendmodel.model.dto.question;

import lombok.Data;

/**
 * @author: wusihao
 * @date: 2024/12/29 10:18
 * @version: 1.0
 * @description: 题目配置
 */
@Data
public class JudgeConfig {
    // 时间限制(ms)
    private Long timeLimit;
    // 空间限制(kb)
    private Long memoryLimit;
    // 堆栈限制(kb)
    private Long stackLimit;

}
