package com.hell.core.command;


import javax.servlet.http.HttpServletRequest;

public interface Command {
    void execute(HttpServletRequest request, Object o) throws Exception;
}
