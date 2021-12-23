package com.hell.action.service.app;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SAppDao;
import com.hell.db.table.provider.SApp;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.UpdateAppRequest;
import com.hell.dto.response.app.AppResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "api")
public class UpdateAppAction implements BaseAction<UpdateAppRequest, AppResponse> {
    private SAppDao appDao;

    @PutMapping(path = "/api/t1/service/updateApp")
    @ApiOperation("环境更新")
    @Override
    public AppResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateAppRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        SApp app = appDao.findByNameAndEntSeq(request.getName(), entSeq);
        if (ObjectUtils.isNotEmpty(app) && app.getSeq().compareTo(request.getAppSeq()) != 0) {
            throw new ValidationException(CheckMsg.VALIDATION_NAME_EXIST);
        }
        List<SApp> appList = appDao.findBySeqAndEntSeq(request.getAppSeq(), entSeq);
        appList = appList.stream().map(a -> {
            a.setName(request.getName());
            a.setType(request.getType());
            a.setInfo(request.getInfo());
            a = appDao.save(a);
            return a;
        }).collect(Collectors.toList());
        AppResponse r = new AppResponse();
        r.setApps(appList);
        return r;
    }

    @Autowired
    public void setAppDao(SAppDao appDao) {
        this.appDao = appDao;
    }
}
