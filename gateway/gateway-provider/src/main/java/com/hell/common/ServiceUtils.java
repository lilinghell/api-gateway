package com.hell.common;

import javax.servlet.http.HttpServletRequest;

public class ServiceUtils {

    public static Object getCurrentUser(HttpServletRequest request) {
        Object user = null == request.getSession(false) ? null : request.getSession(false).getAttribute(Dictionary.SUSER);
        return user;
    }
}
