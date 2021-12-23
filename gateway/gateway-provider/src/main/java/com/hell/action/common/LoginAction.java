package com.hell.action.common;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.dao.SUserDao;
import com.hell.db.dao.common.PRoutersDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.LoginRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "common")
public class LoginAction implements BaseAction<LoginRequest, ResponseResult> {

    private SUserDao userDao;
    private PRoutersDao routersDao;

    @PostMapping(path = "/api/common/login")
    @ApiOperation("登陆")
    @Override
    public ResponseResult execute(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest) throws Exception {
        //判断用户名和密码
        SUser user = userDao.qryUserInfo(loginRequest);
        if (null == user) {
            throw new ValidationException(CheckMsg.VALIDATION_LOGIN_AUTH_ERROR);
        }
        if((user.getEntInfo().getSeq()+"_sysadmin").equals(user.getRoleInfo().getRoleName())) {
            //企业超级管理员（认证人员）
            user.getRoleInfo().setRouters(routersDao.findByServiceType(Dictionary.SERVCIE_TYPE_0).getRouters());
        }
        //放到session中
        request.getSession().setAttribute(Dictionary.SUSER, user);
        SUser nUser = new SUser();
        BeanUtils.copyProperties(user, nUser);
        nUser.setPassword("");

        return new ResponseResult(nUser);
    }

    @Scope("session")
    public SUser getUser() {
        return null;
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setRoutersDao(PRoutersDao routersDao) {
        this.routersDao = routersDao;
    }
}
