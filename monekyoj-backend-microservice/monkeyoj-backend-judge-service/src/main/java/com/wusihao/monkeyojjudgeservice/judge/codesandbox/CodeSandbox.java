package com.wusihao.monkeyojjudgeservice.judge.codesandbox;


import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeRequest;
import com.wusihao.monkeyojbackendmodel.model.codesandbox.ExecuteCodeResponse;

public interface CodeSandbox {
    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
