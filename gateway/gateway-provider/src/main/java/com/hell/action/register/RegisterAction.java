package com.hell.action.register;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.common.service.SmsService;
import com.hell.dao.SEntInfoDao;
import com.hell.dao.SRoleDao;
import com.hell.dao.SUserDao;
import com.hell.db.dao.common.PRoutersDao;
import com.hell.db.table.common.PSmsAuth;
import com.hell.db.table.provider.SEntInfo;
import com.hell.db.table.provider.SRole;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.RegisterRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "register")
public class RegisterAction implements BaseAction<RegisterRequest, ResponseResult> {

    private SUserDao userDao;
    private SmsService smsService;
    private PRoutersDao routersDao;
    private SRoleDao roleDao;
    private SEntInfoDao entInfoDao;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/register")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @RequestBody @Valid RegisterRequest request) throws Exception {
        //校验短信验证码
        PSmsAuth s = new PSmsAuth();
        s.setMobilePhone(request.getMobilePhone());
        s.setApiPath("/api/register");
        s.setCode(request.getCode());
        s.setServiceType(Dictionary.SERVCIE_TYPE_0);
        boolean b = smsService.VerifySms(s);
        if (false == b) {
            throw new ValidationException(CheckMsg.VALIDATION_MB_CODE_ERROR);
        }
        SUser user = userDao.findByMobilePhone(request.getMobilePhone());
        if (null != user) {
            throw new ValidationException(CheckMsg.VALIDATION_MOBILEPHONE_EXIST);
        }

        //创建企业
        SEntInfo e = new SEntInfo();
        e = entInfoDao.save(e);

        //创建角色
        SRole role = new SRole();
        role.setRoleName(e.getSeq()+"_sysadmin");//超级管理员
        role.setEntSeq(e.getSeq());
        role = roleDao.save(role);

        //创建用户
        SUser u = new SUser();
        u.setMobilePhone(request.getMobilePhone());
        u.setPassword(request.getPassword());
        u.setUserState(Dictionary.USER_STATE_0);
        u.setDevType(request.getDevType());
        u.setUserType(Dictionary.USER_TYPE_1);
        u.setEntInfo(e);
        u.setRoleInfo(role);
        u = userDao.save(u);

        //创建用户的seq放到seq中
        httpServletRequest.getSession().setAttribute("_registerUserSeq", u.getSeq());
        httpServletRequest.getSession().setAttribute("_registerEntSeq", u.getEntInfo().getSeq());
        return new ResponseResult("success");
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }

    @Autowired
    public void setRoutersDao(PRoutersDao routersDao) {
        this.routersDao = routersDao;
    }

    @Autowired
    public void setRoleDao(SRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setEntInfoDao(SEntInfoDao entInfoDao) {
        this.entInfoDao = entInfoDao;
    }
}
