package com.wusihao.monkeyoj.judge.codesandbox;

import com.wusihao.monkeyoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyoj.judge.codesandbox.model.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
