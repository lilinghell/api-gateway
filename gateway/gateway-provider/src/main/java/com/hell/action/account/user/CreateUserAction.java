package com.hell.action.account.user;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SEntInfo;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.CreateUserRequest;
import com.hell.dto.response.UserResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "user api")
public class CreateUserAction implements BaseAction<CreateUserRequest, UserResponse> {
    private SUserDao userDao;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/t1/account/createUser")
    @ApiOperation("创建用户")
    @Override
    public UserResponse execute(HttpServletRequest re, @Valid @RequestBody CreateUserRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(re);
        SEntInfo entInfo = user.getEntInfo();

        SUser u = userDao.findByEmail(request.getEmail());
        if (null != u) {
            throw new ValidationException(CheckMsg.VALIDATION_EMAIL_EXIST);
        }
        u = userDao.findByUserId(request.getUserId());
        if (null != u) {
            throw new ValidationException(CheckMsg.VALIDATION_USERID_EXIST);
        }

        SUser newUser = new SUser();
        BeanUtils.copyProperties(request, newUser);
        newUser.setUserState(Dictionary.USER_STATE_0);
        newUser.setEntInfo(entInfo);
        newUser.setDevType(Dictionary.DEV_TYPE_1);
        newUser.setUserType(Dictionary.USER_TYPE_2);
        newUser.setRoleInfo(request.getRole());
        newUser = userDao.save(newUser);

        UserResponse result = new UserResponse();
        result.setUser(newUser);
        return result;
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
