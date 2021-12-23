package com.hell.action.service.app;

import com.hell.common.ServiceUtils;
import com.hell.dao.SAppDao;
import com.hell.db.table.provider.SApp;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.QryAppRequest;
import com.hell.dto.response.app.AppResponse;
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

@Api(tags = "env")
@RestController
public class QryAppAction implements BaseAction<QryAppRequest, AppResponse> {

    private SAppDao appDao;

    @GetMapping(path = "/api/t1/service/qryApp")
    @ApiOperation("环境查询")
    @Override
    public AppResponse execute(HttpServletRequest httpServletRequest, QryAppRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SApp> apps = null;
        if(ObjectUtils.isNotEmpty(request.getAppSeq())) {
            apps = appDao.findBySeqAndEntSeq(request.getAppSeq(), entSeq);
        } else {
            apps = appDao.findByEntSeq(entSeq, Sort.by(Sort.Direction.DESC,"seq"));
        }
        AppResponse r = new AppResponse();
        r.setApps(apps);

        return r;
    }

    @Autowired
    public void setAppDao(SAppDao appDao) {
        this.appDao = appDao;
    }
}
