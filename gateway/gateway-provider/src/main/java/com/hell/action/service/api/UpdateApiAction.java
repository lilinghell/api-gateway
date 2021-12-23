package com.hell.action.service.api;

import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiGroupDao;
import com.hell.dao.SApiHisDao;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.*;
import com.hell.dto.request.UpdateApiRequest;
import com.hell.dto.response.ApiResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "api")
public class UpdateApiAction implements BaseAction<UpdateApiRequest, ApiResponse> {

    private SApiGroupDao apiGroupDao;
    private SApiDao apiDao;
    private SApiHisDao apiHisDao;
    private SAppEnvDao appEnvDao;

    @PutMapping(path = "/api/t1/service/updateApi")
    @ApiOperation("API更新")
    @Override
    public ApiResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateApiRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SAppEnv> appEnvs = appEnvDao.findBySeqAndEntSeq(request.getAppEnvSeq(), entSeq);
        SAppEnv appEnv = appEnvs.get(0);
        if (appEnv.getServiceType().equals(Dictionary.SERVICE_TYPE_1)) {
            //微服务
            if (StringUtils.isEmpty(request.getServiceId())) {
                throw new ValidationException(CheckMsg.VALIDATION_SERVICE_ID_NOT_NULl);
            }
        }

        SApi apiExit = apiDao.findByUrlAndAppEnvSeq(request.getUrl(), request.getAppEnvSeq());
        if (ObjectUtils.isNotEmpty(apiExit) && !apiExit.getSeq().equals(request.getSeq())) {
            throw new ValidationException(CheckMsg.VALIDATION_URL_IS_EXIT);
        }

        SApi api = apiDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        //保存到历史表
        SApiHis apiHis = new SApiHis();
        BeanUtils.copyProperties(api, apiHis);
        apiHisDao.save(apiHis);

        BeanUtils.copyProperties(request, api);
        if (request.getClickTabFlag().equals("define") || request.getClickTabFlag().equals("develop")) {
            api.setSyncGateway(Dictionary.API_SYNC_GATEWAY_1);//待同步gateway
        }

        if (ObjectUtils.isNotEmpty(request.getGroupSeq())) {
            SApiGroup group = apiGroupDao.findBySeqAndEntSeq(request.getGroupSeq(), entSeq);
            api.setGroup(group);
        }

        apiDao.save(api);

        ApiResponse r = new ApiResponse();
        r.setApi(api);

        return r;
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setApiHisDao(SApiHisDao apiHisDao) {
        this.apiHisDao = apiHisDao;
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
