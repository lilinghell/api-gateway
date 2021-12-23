package com.hell.db.dao.common;

import com.hell.db.table.common.PSmsAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsAuthDao extends JpaRepository<PSmsAuth, Integer> {


    PSmsAuth findByMobilePhoneAndApiPathAndServiceType(String mobilePhone, String apiPath, String serviceType);

    PSmsAuth findByMobilePhoneAndApiPathAndCodeAndServiceType(String mobilePhone, String apiPath, String code, String serviceType);
}
