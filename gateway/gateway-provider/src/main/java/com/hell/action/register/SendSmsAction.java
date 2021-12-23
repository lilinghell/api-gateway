package com.hell.action.register;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.common.service.SmsService;
import com.hell.dao.SUserDao;
import com.hell.db.table.common.PSmsAuth;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.SendSmsRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@Api(tags = "common")
public class SendSmsAction implements BaseAction<SendSmsRequest, ResponseResult> {

    private SUserDao userDao;

    @Resource
    private SmsService smsService;

    @PostMapping(path = "/api/register/sendSms")
    @ApiOperation("发送短信")
    @Override
    public ResponseResult execute(@RequestBody @Valid SendSmsRequest request) throws Exception {
        SUser user = userDao.findByMobilePhone(request.getMobilePhone());
        if (null != user) {
            throw new ValidationException(CheckMsg.VALIDATION_MOBILEPHONE_EXIST);
        }

        PSmsAuth s = new PSmsAuth();
        s.setMobilePhone(request.getMobilePhone());
        s.setGlobalRoaming(request.getGlobalRoaming());
        s.setApiPath("/api/register");
        s.setServiceType(Dictionary.SERVCIE_TYPE_0);
        smsService.sendSms(s, "注册短信验证码:{code}");

        return new ResponseResult("success");
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
