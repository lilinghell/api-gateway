package com.hell.config.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

public interface BaseAction<Req, Res> {

    default Res execute(@Valid Req request) throws Exception {
        return null;
    }

    default Res execute(HttpServletRequest httpServletRequest, @Valid Req request) throws Exception {
        return null;
    }

    default Res execute(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @Valid Req request) throws Exception {
        return null;
    }

    default Res execute() throws Exception {
        return null;
    }
}
