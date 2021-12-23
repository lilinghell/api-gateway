package com.hell.core.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public interface BaseHandlerInterceptor extends HandlerInterceptor {

    List<String> getPathPatterns();

    List<String> getExcludePathPatterns();
}
