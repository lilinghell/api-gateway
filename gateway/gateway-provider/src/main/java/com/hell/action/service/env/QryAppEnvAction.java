package com.hell.action.service.env;

import com.hell.common.ServiceUtils;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SAppEnv;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.AppEnvRequest;
import com.hell.dto.response.app.AppEnvResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = "app")
public class QryAppEnvAction implements BaseAction<AppEnvRequest, AppEnvResponse> {
    private SAppEnvDao appEnvDao;

    @GetMapping(path = "/api/t1/service/qryAppEnv")
    @ApiOperation("生态环境查询")
    @Override
    public AppEnvResponse execute(HttpServletRequest httpServletRequest, AppEnvRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<SAppEnv> envList;
        if(ObjectUtils.isNotEmpty(request.getAppEnvSeq())) {
            envList = appEnvDao.findBySeqAndEntSeq(request.getAppEnvSeq(), entSeq);
        } else {
            envList = appEnvDao.findByAppSeqAndEntSeq(request.getAppSeq(), entSeq, Sort.by(Sort.Direction.DESC,"seq"));
        }
        AppEnvResponse r = new AppEnvResponse();
        r.setAppEnvs(envList);
        return r;
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
