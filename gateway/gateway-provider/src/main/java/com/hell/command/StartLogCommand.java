package com.hell.command;

import com.hell.core.command.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component("StartLogCommand")
public class StartLogCommand implements Command {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Override
    public void execute(HttpServletRequest request, Object o) throws Exception {
        log.info(request.getRequestURI() + " StartLogCommand=====" + o);
    }
}
