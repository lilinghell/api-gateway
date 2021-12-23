package com.hell.action.service.api;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ApiExtRequest;
import com.hell.dto.response.ApiResponse;
import com.hell.service.NotifyGatewayService;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "api")
public class SyncGatewayAction implements BaseAction<ApiExtRequest, ApiResponse> {
    private SApiDao apiDao;
    private NotifyGatewayService notifyGatewayService;

    @PostMapping(path = "/api/t1/service/syncGateway")
    @ApiOperation("API同步网关")
    @Override
    public ApiResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody ApiExtRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApi api = apiDao.findBySeqAndEntSeq(request.getApiSeq(), entSeq);
        api.setSyncGateway(Dictionary.API_SYNC_GATEWAY_0);//已同步状态
        apiDao.save(api);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setApi(api);
        //同步
        notifyGatewayService.notifyGateway(api.getSeq(), api.getUrl(), api.getAppEnvSeq());
        return apiResponse;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setNotifyGatewayService(NotifyGatewayService notifyGatewayService) {
        this.notifyGatewayService = notifyGatewayService;
    }
}
