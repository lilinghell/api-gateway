package com.hell.action.account.user;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.UpdatePassRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "user api")
public class UpdatePassAction implements BaseAction<UpdatePassRequest, ResponseResult> {

    private SUserDao userDao;

    @ApiOperation("修改当前用户的登陆密码")
    @PutMapping(path = "/api/t1/account/updatePassword")
    @Override
    public ResponseResult execute(HttpServletRequest request, @RequestBody @Valid UpdatePassRequest re) throws Exception {

        if (!re.getConfirmPassword().equals(re.getNewPassword())) {
            throw new ValidationException(CheckMsg.VALIDATION_PASS_MATCH_ERROR);
        }
        SUser user = (SUser) ServiceUtils.getCurrentUser(request);
        SUser u = userDao.findById(user.getSeq()).get();
        if (!u.getPassword().equals(re.getOldPassword())) {
            throw new ValidationException(CheckMsg.VALIDATION_PASS_ERROR);
        }
        u.setPassword(re.getConfirmPassword());
        userDao.save(u);
        return new ResponseResult("success");
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
