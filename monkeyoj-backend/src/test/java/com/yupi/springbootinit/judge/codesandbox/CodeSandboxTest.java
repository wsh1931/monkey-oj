package com.yupi.springbootinit.judge.codesandbox;

import com.yupi.monkeyoj.job.judge.codesandbox.CodeSandbox;
import com.yupi.monkeyoj.job.judge.codesandbox.impl.ExampleSandboxImpl;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.yupi.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;
import com.yupi.monkeyoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:27
 * @version: 1.0
 * @description: 代码沙箱测试类
 */
@SpringBootTest
public class CodeSandboxTest {
    @Test
    void executeCode() {
        String code = "int main () {}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();

        CodeSandbox exampleSandbox = new ExampleSandboxImpl();
        ExecuteCodeResponse executeCodeResponse = exampleSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }
}
