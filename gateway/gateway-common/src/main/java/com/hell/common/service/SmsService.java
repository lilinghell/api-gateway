package com.hell.common.service;

import com.hell.common.CheckMsg;
import com.hell.db.dao.common.SmsAuthDao;
import com.hell.db.table.common.PSmsAuth;
import com.hell.core.exception.ValidationException;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Data
public class SmsService {

    private int numSecurity = 20;
    private int randomCount = 6;
    private int timeSecurity = 5;//分钟
    private int timeOut = 5;//分钟

    private SmsAuthDao smsAuthDao;

    public void sendSms(PSmsAuth smsAuth, String msg) throws ValidationException {
        if (StringUtils.isEmpty(smsAuth.getApiPath()) ||
                StringUtils.isEmpty(smsAuth.getMobilePhone()) ||
                        StringUtils.isEmpty(smsAuth.getServiceType())) {
            throw new ValidationException(CheckMsg.VALIDATION_REQUEST_IS_NULL);
        }
        PSmsAuth s = smsAuthDao.findByMobilePhoneAndApiPathAndServiceType(smsAuth.getMobilePhone(), smsAuth.getApiPath(), smsAuth.getServiceType());
        //生成6短信验证码
        String code = RandomStringUtils.randomNumeric(randomCount);

        if (ObjectUtils.isNotEmpty(s)) {
            //5分钟内发送超过20次，则提示不能发送
            if (s.getNum() >= numSecurity && new Date(s.getUpdateTime().getTime() + timeSecurity * 60 * 1000)
                    .after(new Date())) {
                throw new ValidationException(CheckMsg.VALIDATION_FREQUENT_OPERATION);
            }
            if (s.getNum() >= numSecurity) {
                s.setNum(1);
            } else {
                s.setNum(s.getNum() + 1);
            }
            s.setCode(code);
            smsAuthDao.save(s);
        } else {
            smsAuth.setNum(1);
            smsAuth.setCode(code);
            smsAuthDao.save(smsAuth);
        }
        //todo 发送短信
        String m = StringUtils.replaceChars(msg, "{code}", code);//发送的内容
        System.out.println("短信发送内容：" + m);
    }

    public boolean VerifySms(PSmsAuth smsAuth) {
        PSmsAuth s = smsAuthDao.findByMobilePhoneAndApiPathAndCodeAndServiceType(smsAuth.getMobilePhone(), smsAuth.getApiPath(), smsAuth.getCode(), smsAuth.getServiceType());
        if (null != s) {
            smsAuthDao.delete(s);
            if (new Date(s.getUpdateTime().getTime() + timeOut * 60 * 1000).before(new Date())) {
                // 超时
                return false;
            }
            return true;
        } else {
            return false;
        }

    }

    @Autowired
    public void setSmsAuthDao(SmsAuthDao smsAuthDao) {
        this.smsAuthDao = smsAuthDao;
    }
}
