package ms.frame.core.context;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class MessageContext {

    private static final String _REQUEST_BODY_ATTRIBUTE_NAME = "_REQUEST_SERVICE_BODY_ATTRIBUTE_NAME";

    public static void setRequestBody(Object body) {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        requestAttributes.setAttribute(_REQUEST_BODY_ATTRIBUTE_NAME, body, RequestAttributes.SCOPE_REQUEST);
    }

    public static Object getRequestBody() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        return requestAttributes.getAttribute(_REQUEST_BODY_ATTRIBUTE_NAME, RequestAttributes.SCOPE_REQUEST);
    }
}
