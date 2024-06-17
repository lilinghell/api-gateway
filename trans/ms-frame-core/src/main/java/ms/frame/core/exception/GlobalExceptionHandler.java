package ms.frame.core.exception;

import ms.frame.core.common.BaseCheckMsg;
import ms.frame.core.common.SysDict;
import ms.frame.core.context.LocaleMessage;
import ms.frame.core.response.Response;
import ms.frame.core.response.ResponseHead;
import ms.frame.core.response.ResponseVoFormat;
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

@ControllerAdvice
public class GlobalExceptionHandler {
    protected static Log log = LogFactory.getLog(GlobalExceptionHandler.class);
    @Resource
    private LocaleMessage localeMessage;

    @Resource
    private ResponseVoFormat responseVoFormat;

    @ExceptionHandler(value = ValidationException.class)
    @ResponseBody
    public Response<?> ValidationExceptionHandler(HttpServletRequest request, ValidationException e) {
        String str = this.localeMessage.getMessage(e.getErrorCode(), e.getArgs());
        log.error("ValidationException:" + str, e);
        e.setErrorCode(e.getErrorCode());
        e.setErrorMsg(str);

        saveRequestError(request, e);
        return resolverException(e);
    }

    @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
    @ResponseBody
    public Response<?> handleValidationException(HttpServletRequest request, Exception e) {
        List<FieldError> fieldErrors = null;
        if (e instanceof BindException) {
            fieldErrors = ((BindException) e).getFieldErrors();
        } else if (e instanceof MethodArgumentNotValidException) {
            fieldErrors = ((MethodArgumentNotValidException) e).getBindingResult().getFieldErrors();
        }
        StringBuilder msg = new StringBuilder();
        fieldErrors.forEach(fieldError -> {
            msg.append(this.localeMessage.getMessage(fieldError.getField())).append("-").append(this.localeMessage.getMessage(fieldError.getDefaultMessage())).append(",");
        });
        String reMsg = msg.toString();
        if (StringUtils.isNotEmpty(reMsg)) {
            reMsg = reMsg.substring(0, reMsg.length() - 1);
        }
        log.error(e.getClass().getSimpleName() + ":" + reMsg);

        ValidationException e2 = new ValidationException();
        e2.setErrorCode(BaseCheckMsg.BAD_REQUEST);
        e2.setErrorMsg(reMsg);

        saveRequestError(request, e2);
        return resolverException(e2);
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<?> exceptionHandler(HttpServletRequest request, Exception e) {
        String reMsg = this.localeMessage.getMessage(BaseCheckMsg.UNKNOWN_FAILURE);
        log.error("Exception: " + reMsg, e);

        ValidationException e2 = new ValidationException();
        e2.setErrorCode(BaseCheckMsg.UNKNOWN_FAILURE);
        e2.setErrorMsg(reMsg);

        saveRequestError(request, e2);
        return resolverException(e2);
    }



    public Response<?> resolverException(ValidationException e) {
        ResponseHead head = responseVoFormat.responseHeadFormat(e.getErrorCode(), e.getErrorMsg());

        Response response = new Response();
        response.setHead(head);
        return response;
    }

    private void saveRequestError(HttpServletRequest request, ValidationException e) {
        request.setAttribute(SysDict.ERROR_CODE, e.getErrorCode());
        request.setAttribute(SysDict.ERROR_MSG, e.getErrorMsg());
        request.setAttribute(SysDict.JNL_STATUS, SysDict.JNL_STATUS_1);
    }
}
