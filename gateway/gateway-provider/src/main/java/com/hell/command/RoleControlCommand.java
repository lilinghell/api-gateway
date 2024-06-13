package com.hell.command;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.core.command.Command;
import com.hell.core.exception.ValidationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验
 */
@Component("RoleControlCommand")
public class RoleControlCommand implements Command {

    @Override
    public void execute(HttpServletRequest request, Object o) throws Exception {
        Object user = null == request.getSession(false) ? null : request.getSession(false).getAttribute(Dictionary.SUSER);

        if (null == user) {
            throw new ValidationException(CheckMsg.VALIDATION_AUTH);
        }
        request.setAttribute("_USER", user);
        //

    }
}
