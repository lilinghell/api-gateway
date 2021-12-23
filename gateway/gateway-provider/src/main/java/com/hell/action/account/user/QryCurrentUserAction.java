package com.hell.action.account.user;

import com.hell.common.ServiceUtils;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.QryUserRequest;
import com.hell.dto.response.UserResponse;
import com.hell.config.action.BaseAction;
import com.hell.config.request.BaseRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "user api")
public class QryCurrentUserAction implements BaseAction<BaseRequest, UserResponse> {


    @GetMapping(path = "/api/t1/account/qryCurrentUser")
    @ApiOperation("查询当前登陆用户")
    public UserResponse execute(HttpServletRequest re, @Valid QryUserRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(re);

        SUser newUser = new SUser();
        BeanUtils.copyProperties(user, newUser);
        newUser.setPassword("");

        UserResponse ur = new UserResponse();
        ur.setUser(newUser);

        return ur;
    }
}
