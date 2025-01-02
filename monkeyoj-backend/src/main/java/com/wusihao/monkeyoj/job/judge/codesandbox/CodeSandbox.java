package com.wusihao.monkeyoj.job.judge.codesandbox;

import com.wusihao.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
