package com.hell.action.service.api;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiGroupDao;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.AddApiRequest;
import com.hell.dto.response.ApiResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "api")
public class AddApiAction implements BaseAction<AddApiRequest, ApiResponse> {

    private SApiDao apiDao;
    private SApiGroupDao apiGroupDao;
    private SAppEnvDao appEnvDao;

    @PostMapping(path = "/api/t1/service/addApi")
    @ApiOperation("API添加")
    @Override
    public ApiResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddApiRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApi apiExit = apiDao.findByNameAndGroupSeqAndEntSeqAndAppEnvSeq(request.getName(),request.getGroupSeq(),entSeq,request.getAppEnvSeq());
        if (ObjectUtils.isNotEmpty(apiExit)) {
            throw new ValidationException(CheckMsg.VALIDATION_NAME_EXIST);
        }

        ApiResponse r = new ApiResponse();

        SApi api = new SApi();
        BeanUtils.copyProperties(request, api);
        api.setEntSeq(entSeq);

        SApiGroup apiGroup = apiGroupDao.findBySeqAndEntSeq(request.getGroupSeq(), entSeq);
        api.setGroup(apiGroup);

        api = apiDao.save(api);
        r.setApi(api);
        return r;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }

    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
