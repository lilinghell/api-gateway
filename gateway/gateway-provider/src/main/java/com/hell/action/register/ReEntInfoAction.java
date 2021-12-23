package com.hell.action.register;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.common.service.UploaderService;
import com.hell.dao.SEntInfoDao;
import com.hell.db.table.common.PAttachment;
import com.hell.db.table.provider.SEntInfo;
import com.hell.dto.request.ReEntInfoRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "register")
public class ReEntInfoAction implements BaseAction<ReEntInfoRequest, ResponseResult> {

    private UploaderService uploader;
    private SEntInfoDao entDao;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/register/createEntInfo")
    @ApiOperation("完善企业信息")
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid ReEntInfoRequest request) throws Exception {
        //获取注册的用户seq
        if (ObjectUtils.isEmpty(request.getCorpFile0())) {
            throw new ValidationException(CheckMsg.VALIDATION_CORPFILE0_IS_NULL);
        }
        if (ObjectUtils.isEmpty(request.getCorpFile1())) {
            throw new ValidationException(CheckMsg.VALIDATION_CORPFILE1_IS_NULL);
        }
        if (ObjectUtils.isEmpty(request.getEntLicenseFile())) {
            throw new ValidationException(CheckMsg.VALIDATION_ENTLICENSEFILE_IS_NULL);
        }

        SEntInfo ent = entDao.findByEntCode(request.getEntCode());
        if (ObjectUtils.isNotEmpty(ent)) {
            throw new ValidationException(CheckMsg.VALIDATION_ENT_EXIST);
        }

        Object userSeq = httpServletRequest.getSession(false) == null ? null : httpServletRequest.getSession(false).getAttribute("_registerUserSeq");
        if (ObjectUtils.isEmpty(userSeq)) {
            throw new ValidationException(CheckMsg.VALIDATION_UNAUTH_OPERATION);
        }
        Object entSeq = httpServletRequest.getSession(false) == null ? null : httpServletRequest.getSession(false).getAttribute("_registerEntSeq");
        if (ObjectUtils.isEmpty(entSeq)) {
            throw new ValidationException(CheckMsg.VALIDATION_UNAUTH_OPERATION);
        }

        ent = entDao.findById((Integer) entSeq).get();
        BeanUtils.copyProperties(request, ent);
        ent.setStatus(Dictionary.ENT_STATUS_9);

        PAttachment corpFile0 = uploader.singleUpload("/certificates/", request.getCorpFile0());
        ent.setCorpFile0(corpFile0);
        PAttachment corpFile1 = uploader.singleUpload("/certificates/", request.getCorpFile1());
        ent.setCorpFile1(corpFile1);
        PAttachment entLicenseFile = uploader.singleUpload("/certificates/", request.getEntLicenseFile());
        ent.setEntLicenseFile(entLicenseFile);

        entDao.save(ent);

        httpServletRequest.getSession(false).removeAttribute("_registerUserSeq");
        httpServletRequest.getSession(false).removeAttribute("_registerEntSeq");

        return new ResponseResult("success");
    }

    @Autowired
    public void setUploader(UploaderService uploader) {
        this.uploader = uploader;
    }

    @Autowired
    public void setEntDao(SEntInfoDao entDao) {
        this.entDao = entDao;
    }
}
