package com.hell.action.account.user;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.UpdateUserRequest;
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
public class UpdateUserAction implements BaseAction<UpdateUserRequest, UserResponse> {

    private SUserDao userDao;

    @PutMapping(path = "/api/t1/account/updateUser")
    @ApiOperation("用户更新")
    @Override
    public UserResponse execute(HttpServletRequest r, @Valid @RequestBody UpdateUserRequest request) throws Exception {

        if (StringUtils.isEmpty(request.getEmail()) && StringUtils.isEmpty(request.getMobilePhone())
                && StringUtils.isEmpty(request.getUserId())) {
            throw new ValidationException(CheckMsg.VALIDATION_USERINFO_ISNULL);
        }
        SUser u = userDao.findByEmail(request.getEmail());
        if (null != u && (request.getSeq()).compareTo(u.getSeq()) != 0) {
            throw new ValidationException(CheckMsg.VALIDATION_EMAIL_EXIST);
        }
        u = userDao.findByUserId(request.getUserId());
        if (null != u && (request.getSeq()).compareTo(u.getSeq()) != 0) {
            throw new ValidationException(CheckMsg.VALIDATION_USERID_EXIST);
        }
        u = userDao.findByMobilePhone(request.getMobilePhone());
        if (null != u && (request.getSeq()).compareTo(u.getSeq()) != 0) {
            throw new ValidationException(CheckMsg.VALIDATION_MOBILEPHONE_EXIST);
        }

        SUser us = (SUser) ServiceUtils.getCurrentUser(r);

        u = userDao.findBySeqAndEntInfoSeq(request.getSeq(), us.getEntInfo().getSeq());

        if (StringUtils.isEmpty(request.getPassword())) {
            request.setPassword(u.getPassword());
        }
        BeanUtils.copyProperties(request, u);

        userDao.save(u);

        u.setPassword("");

        UserResponse re = new UserResponse();
        re.setUser(u);
        return re;
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
