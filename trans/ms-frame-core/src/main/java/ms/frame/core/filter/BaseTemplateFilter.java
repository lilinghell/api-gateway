package ms.frame.core.filter;

import ms.frame.core.http.HttpServletRequestWrapperExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 该Filter主要指定了自定义的requestWrapper
 */
@Component
public class BaseTemplateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String contentType = StringUtils.lowerCase(servletRequest.getContentType());
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            ServletRequest requestWrapper = new HttpServletRequestWrapperExt(httpServletRequest);
            filterChain.doFilter(requestWrapper, servletResponse);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
