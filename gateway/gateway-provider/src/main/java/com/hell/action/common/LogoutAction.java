package com.hell.action.common;

import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "common")
public class LogoutAction {

    @PostMapping(path = "/api/common/logout")
    @ApiOperation("登出")
    public ResponseResult execute(HttpServletRequest request) throws Exception {
        request.getSession().invalidate();
        return new ResponseResult("success");
    }
}
