package ms.frame.core.context;

import ms.frame.core.common.BaseCheckMsg;
import ms.frame.core.context.LocaleMessage;
import ms.frame.core.response.Response;
import ms.frame.core.response.ResponseHead;
import ms.frame.core.response.ResponseVoFormat;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;

import javax.annotation.Resource;

@ControllerAdvice
public class ResponseBodyAdvice implements org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice<Response> {
    @Resource
    private LocaleMessage localeMessage;

    @Resource
    private ResponseVoFormat responseVoFormat;

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        Class<?> newClass = methodParameter.getNestedParameterType();
        return Response.class.isAssignableFrom(newClass);
    }

    @Override
    public Response<?> beforeBodyWrite(Response body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (null == body) {
            body = new Response();
        }
        if (ObjectUtils.isEmpty(body.getHead())) {
            String m = this.localeMessage.getMessage(BaseCheckMsg.SUCCESS);
            ResponseHead head = responseVoFormat.responseHeadFormat(BaseCheckMsg.SUCCESS, m);
            body.setHead(head);
        }
        return body;
    }
}
