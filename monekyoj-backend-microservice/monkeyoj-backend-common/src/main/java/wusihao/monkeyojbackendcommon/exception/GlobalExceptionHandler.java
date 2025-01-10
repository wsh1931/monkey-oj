package wusihao.monkeyojbackendcommon.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wusihao.monkeyojbackendcommon.common.BaseResponse;
import wusihao.monkeyojbackendcommon.common.ErrorCode;
import wusihao.monkeyojbackendcommon.common.ResultUtils;

/**
 * 全局异常处理器
 *
 * @author <a href="https://github.com/wsh1931">吴思豪</a>
 *
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
