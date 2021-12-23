package com.hell.action.account.user;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.UpdateCurrentUserRequest;
import com.hell.dto.response.UserResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "user api")
public class UpdateCurrentUserAction implements BaseAction<UpdateCurrentUserRequest, UserResponse> {
    private SUserDao userDao;

    @PutMapping(path = "/api/t1/account/updateCurrentUser")
    @ApiOperation("当前用户更新")
    @Override
    public UserResponse execute(HttpServletRequest r, @Valid @RequestBody UpdateCurrentUserRequest request) throws Exception {
        SUser u;
        if (StringUtils.isNotEmpty(request.getEmail())) {
            u = userDao.findByEmail(request.getEmail());
            if (null != u && u.getSeq() != request.getSeq()) {
                throw new ValidationException(CheckMsg.VALIDATION_EMAIL_EXIST);
            }
        }
        if (StringUtils.isNotEmpty(request.getUserId())) {
            u = userDao.findByUserId(request.getUserId());
            if (null != u && u.getSeq() != request.getSeq()) {
                throw new ValidationException(CheckMsg.VALIDATION_USERID_EXIST);
            }
        }
        if (StringUtils.isNotEmpty(request.getMobilePhone())) {
            u = userDao.findByMobilePhone(request.getMobilePhone());
            if (null != u && u.getSeq() != request.getSeq()) {
                throw new ValidationException(CheckMsg.VALIDATION_MOBILEPHONE_EXIST);
            }
        }

        SUser us = (SUser) ServiceUtils.getCurrentUser(r);
        if (StringUtils.isEmpty(request.getPassword())) {
            request.setPassword(us.getPassword());
        }
        BeanUtils.copyProperties(request, us);
        userDao.save(us);

        SUser nUser = new SUser();
        BeanUtils.copyProperties(us, nUser);
        nUser.setPassword("");

        UserResponse uR = new UserResponse();
        uR.setUser(nUser);
        return uR;
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
