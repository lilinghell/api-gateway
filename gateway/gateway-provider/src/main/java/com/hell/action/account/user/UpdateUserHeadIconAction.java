package com.hell.action.account.user;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.common.service.UploaderService;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import com.hell.dao.SUserDao;
import com.hell.db.table.common.PAttachment;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.UpdateUserHeadIconRequest;
import com.hell.dto.response.UserResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "user api")
public class UpdateUserHeadIconAction implements BaseAction<UpdateUserHeadIconRequest, UserResponse> {
    private UploaderService uploader;
    private SUserDao userDao;

    @PostMapping(path = "/api/t1/account/uploadUserHeadIcon")
    @ApiOperation("用户头像更新")
    public UserResponse execute(HttpServletRequest r, @Valid UpdateUserHeadIconRequest request) throws Exception {
        SUser cUser = (SUser) ServiceUtils.getCurrentUser(r);
        SUser u = userDao.findById(request.getUserSeq()).get();

        if (cUser.getEntInfo().getSeq() != u.getEntInfo().getSeq()) {
            //防越权，只能删除本企业
            throw new ValidationException(CheckMsg.VALIDATION_UNAUTH_OPERATION);
        }

        PAttachment p = uploader.singleUpload("/images/", request.getFile());
        u.setHeadIcon(p);
        userDao.save(u);

        UserResponse response = new UserResponse();
        u.setPassword("");
        response.setUser(u);
        return response;
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUploader(UploaderService uploader) {
        this.uploader = uploader;
    }
}
