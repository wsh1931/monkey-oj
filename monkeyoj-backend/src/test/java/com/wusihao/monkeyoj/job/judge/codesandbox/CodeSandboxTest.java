package com.wusihao.monkeyoj.job.judge.codesandbox;

import com.wusihao.monkeyoj.job.judge.codesandbox.impl.ExampleSandboxImpl;
import com.wusihao.monkeyoj.job.judge.codesandbox.model.ExecuteCodeRequest;
import com.wusihao.monkeyoj.job.judge.codesandbox.model.ExecuteCodeResponse;
import com.wusihao.monkeyoj.model.enums.QuestionSubmitLanguageEnum;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @author: wusihao
 * @date: 2025/1/2 11:27
 * @version: 1.0
 * @description: 代码沙箱测试类
 */
@SpringBootTest
public class CodeSandboxTest {
    @Value("${codesandbox.type:example}")
    private String type;
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

    @Test
    void executeCodeByValue() {
        CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(type);
        String code = "int main() {}";
        String language = QuestionSubmitLanguageEnum.JAVA.getValue();
        List<String> inputList = Arrays.asList("1 2", "3 4");
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
        Assertions.assertNotNull(executeCodeResponse);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String string = scanner.next();
            CodeSandbox codeSandbox = CodeSandboxFactory.newInstance(string);
            String code = "int main () {}";
            String language = QuestionSubmitLanguageEnum.JAVA.getValue();
            List<String> inputList = Arrays.asList("1 2", "3 4");
            ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                    .code(code)
                    .language(language)
                    .inputList(inputList)
                    .build();

            ExecuteCodeResponse executeCodeResponse = codeSandbox.executeCode(executeCodeRequest);
            Assertions.assertNotNull(executeCodeResponse);
        }
    }
}
