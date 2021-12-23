package com.hell.action.service.api;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiExtDao;
import com.hell.dao.SApiGroupDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiExt;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ApiExportRequest;
import com.hell.entity.api.ApiExportInfo;
import com.hell.entity.api.ApiInfo;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "api")
public class ApiExportAction implements BaseAction<ApiExportRequest, ResponseResult> {

    private SApiDao apiDao;
    private SApiExtDao apiExtDao;
    private SApiGroupDao apiGroupDao;

    @PostMapping(path = "/api/t1/service/qryApiExport")
    @ApiOperation("API导出信息")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody ApiExportRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        ApiExportInfo apiExportInfo = new ApiExportInfo();

        List<SApi> apiList = new ArrayList<>();
        List<SApiExt> apiExtList = new ArrayList<>();
        List<ApiInfo> apiInfoList = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(request.getSelectApis())) {
            //导出指定的接口
            apiList = apiDao.findByEntSeqAndSeqIn(entSeq, request.getSelectApis());
            apiExtList = apiExtDao.findByEntSeqAndApiSeqIn(entSeq, request.getSelectApis());
        }
        for (SApi api : apiList) {
            ApiInfo apiInfo = new ApiInfo();
            apiInfo.setApiGroupKey(api.getGroup() != null ? api.getGroup().getSeq() : null);

            api.setSyncGateway(Dictionary.API_SYNC_GATEWAY_1);//同步开关
            api.setFlowSwitch("1");//限流开关
            api.setMockSwitch("1");//api mock开关
            api.setGroup(null);
            apiInfo.setApi(api);
            for (SApiExt apiExt : apiExtList) {
                if (api.getSeq().compareTo(apiExt.getApiSeq()) == 0) {
                    if (!request.getApiMockFlg()) {
                        apiExt.setApiMock("");
                    }
                    if (!request.getRateFlg()) {
                        apiExt.setBurstCapacity("");
                        apiExt.setReplenishRate("");
                    }
                    if (!request.getTimeOutFlg()) {
                        apiExt.setConnectTimeout("");
                        apiExt.setResponseTimeout("");
                    }
                    apiInfo.setApiExt(apiExt);
                    break;
                }
            }
            apiInfoList.add(apiInfo);
        }
        apiExportInfo.setApiInfoList(apiInfoList);

        List<SApiGroup> apiGroupList = apiGroupDao.findByEntSeqAndAppSeq(entSeq,
                request.getAppSeq());

        apiExportInfo.setApiGroupList(apiGroupList);

        return new ResponseResult(apiExportInfo);
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setApiExtDao(SApiExtDao apiExtDao) {
        this.apiExtDao = apiExtDao;
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }

}
