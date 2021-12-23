package com.hell.action.account.user;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.common.service.UploaderService;
import com.hell.dao.SUserDao;
import com.hell.db.table.common.PAttachment;
import com.hell.db.table.provider.SUser;
import com.hell.config.action.BaseAction;
import com.hell.config.response.Response;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "user api")
public class UploadCUHeadIconAction implements BaseAction {

    private UploaderService uploader;
    private SUserDao userDao;

    @PostMapping(path = "/api/t1/account/uploadCUHeadIcon")
    @ApiOperation("当前用户头像上传")
    public Response execute(@RequestParam MultipartFile file, HttpServletRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(request);

        user = userDao.findById(user.getSeq()).get();

        PAttachment p = uploader.singleUpload("/images/", file);
        user.setHeadIcon(p);
        userDao.save(user);

        user.setPassword("");
        request.getSession(false).setAttribute(Dictionary.SUSER, user);
        return new ResponseResult(user);
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
