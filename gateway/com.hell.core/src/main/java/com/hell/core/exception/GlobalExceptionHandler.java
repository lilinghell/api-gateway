package com.hell.core.exception;

import com.hell.core.common.dict.Dict;
import com.hell.core.context.LocaleMessage;
import com.hell.config.response.ResponseResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@SuppressWarnings("unchecked")
@ControllerAdvice
public class GlobalExceptionHandler {
    protected static Log log = LogFactory.getLog(GlobalExceptionHandler.class);
    @Resource
    private LocaleMessage localeMessage;

    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public ResponseResult ValidationExceptionHandler(HttpServletRequest request, ValidationException e) {
        log.error("ValidationException:", e);
        String str = this.localeMessage.getMessage(e.getMessageKey(), e.getArgs());
        String[] s = str.split("\\:");
        if (s.length > 1) {
            e.setErrorCode(s[0]);
            e.setErrorMsg(s[1]);
        } else if(s.length ==1) {
            e.setErrorCode("777777");
            e.setErrorMsg(str);
        } else {
            e.setErrorCode(e.getMessageKey());
            e.setErrorMsg(e.getMessageKey() + (e.getArgs() == null ? "" : ":" + e.getArgs().toString()));
        }
        request.setAttribute(Dict.ERROR_CODE, e.getErrorCode());
        request.setAttribute(Dict.ERROR_MSG, e.getErrorMsg());
        request.setAttribute(Dict.JNL_STATUS, Dict.JNL_STATUS_1);
        return new ResponseResult(e.getErrorCode(), e.getErrorMsg(), "");
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public ResponseResult handleValidationException(HttpServletRequest request, Exception e) {
        List<FieldError> fieldErrors = null;
        if(e instanceof  BindException) {
            fieldErrors = ((BindException)e).getFieldErrors();
        }else if(e instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException)e).getBindingResult().getFieldErrors();
        }
        StringBuilder msg = new StringBuilder();
        fieldErrors.forEach(fieldError -> {
            msg.append(this.localeMessage.getMessage(fieldError.getField()))
                    .append("-")
                    .append(this.localeMessage.getMessage(fieldError.getDefaultMessage()))
                    .append(",");
        });
        String reMsg = msg.toString();
        if (StringUtils.isNotEmpty(reMsg)) {
            reMsg = reMsg.substring(0, reMsg.length() - 1);
        }
        log.error(e.getClass().getSimpleName() + ":" + reMsg);
        ResponseResult result = new ResponseResult();
        result.setCode("888888");
        result.setData("");
        result.setMsg(reMsg);
        request.setAttribute(Dict.ERROR_CODE, "888888");
        request.setAttribute(Dict.ERROR_MSG, reMsg);
        request.setAttribute(Dict.JNL_STATUS, Dict.JNL_STATUS_1);
        return result;
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseResult exceptionHandler(HttpServletRequest request, Exception e) {
        log.error("Exception:", e);
        request.setAttribute(Dict.ERROR_CODE, "999999");
        request.setAttribute(Dict.ERROR_MSG, "系统繁忙,请稍后");
        request.setAttribute(Dict.JNL_STATUS, Dict.JNL_STATUS_1);
        return new ResponseResult("999999", "系统繁忙,请稍后", "");
    }
}
