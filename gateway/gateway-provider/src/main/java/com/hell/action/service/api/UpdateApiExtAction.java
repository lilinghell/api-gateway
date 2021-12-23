package com.hell.action.service.api;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiExtDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiExt;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.UpdateApiExtRequest;
import com.hell.dto.response.ApiExtResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "api")
public class UpdateApiExtAction implements BaseAction<UpdateApiExtRequest, ApiExtResponse> {

    private SApiExtDao apiExtDao;
    private SApiDao apiDao;

    @Transactional(rollbackFor = Exception.class)
    @PutMapping(path = "/api/t1/service/updateApiExt")
    @ApiOperation("API扩展更新")
    @Override
    public ApiExtResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateApiExtRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApiExt apiExt = apiExtDao.findByApiSeqAndEntSeq(request.getApiSeq(), entSeq);
        apiExt = apiExt == null ? new SApiExt() : apiExt;
        BeanUtils.copyProperties(request, apiExt);
        apiExt.setEntSeq(entSeq);
        apiExtDao.save(apiExt);
        SApi api = apiDao.findBySeqAndEntSeq(request.getApiSeq(), entSeq);
        api.setSyncGateway(Dictionary.API_SYNC_GATEWAY_1);//待同步gateway
        apiDao.save(api);

        ApiExtResponse r = new ApiExtResponse();
        BeanUtils.copyProperties(apiExt, r);
        r.setSyncGateway(api.getSyncGateway());

        return r;
    }

    @Autowired
    public void setApiExtDao(SApiExtDao apiExtDao) {
        this.apiExtDao = apiExtDao;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }
}
