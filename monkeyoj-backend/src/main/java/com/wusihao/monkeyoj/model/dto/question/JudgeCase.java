package com.wusihao.monkeyoj.model.dto.question;

import lombok.Data;

/**
 * @author: wusihao
 * @date: 2024/12/29 10:18
 * @version: 1.0
 * @description: 题目用例
 */
@Data
public class JudgeCase {
    // 输入用例
    private String input;
    // 输出用例
    private String output;
}
