package com.yupi.monkeyoj.job.judge.codesandbox;

import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
