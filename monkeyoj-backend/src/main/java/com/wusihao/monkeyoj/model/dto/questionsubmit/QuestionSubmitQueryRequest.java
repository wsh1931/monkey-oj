package com.wusihao.monkeyoj.model.dto.questionsubmit;

import com.wusihao.monkeyoj.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询
 *
 * @author <a href="https://github.com/wsh1931">吴思豪</a>
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {
    /**
     * 编程语言
     */
    private String language;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 题目 id
     */
    private Long questionId;
    /**
     * 用户 id
     */
    private Long userId;

    private static final long serialVersionUID = 1L;
}