package com.hell.action.service.api;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiExtDao;
import com.hell.dao.SApiServiceTagDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiExt;
import com.hell.db.table.provider.SApiServiceTag;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ExportToOtherEnvRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "api")
public class ExportToOtherEnvAction implements BaseAction<ExportToOtherEnvRequest, ResponseResult> {

    private SApiDao apiDao;
    private SApiExtDao apiExtDao;
    private SApiServiceTagDao serviceTagDao;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/t1/service/exportToOtherEnv")
    @ApiOperation("API导出信息")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody ExportToOtherEnvRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SApi> apiList = apiDao.findBySeqInAndEntSeqAndAppEnvSeq(request.getApiSeqList(), entSeq, request.getEnvSeq());

        apiList.forEach(importApi -> {
            SApi newApi = new SApi();
            BeanUtils.copyProperties(importApi, newApi);
            newApi.setAppEnvSeq(request.getOtherEnvSeq());
            newApi.setSyncGateway(Dictionary.API_SYNC_GATEWAY_1);
            SApi oldApi = apiDao.findByUrlAndAppEnvSeq(importApi.getUrl(), request.getOtherEnvSeq());
            if (ObjectUtils.isNotEmpty(oldApi)) {
                //存在则更新
                newApi.setSeq(oldApi.getSeq());
                newApi.setMockSwitch(oldApi.getMockSwitch());
                newApi.setFlowSwitch(oldApi.getFlowSwitch());
                newApi.setStatus(oldApi.getStatus());
                newApi = apiDao.save(newApi);
            } else {
                newApi.setSeq(null);
                newApi = apiDao.save(newApi);
            }

            //标签
            List<SApiServiceTag> tagList = serviceTagDao.findByApiSeqAndEntSeq(importApi.getSeq(), entSeq);
            if (ObjectUtils.isNotEmpty(tagList)) {
                SApi finalNewApi = newApi;
                tagList.forEach(sApiServiceTag -> {
                    SApiServiceTag a = new SApiServiceTag();
                    BeanUtils.copyProperties(sApiServiceTag, a);
                    a.setApiSeq(finalNewApi.getSeq());
                    serviceTagDao.save(a);
                });
            }

            SApiExt newApiExt = apiExtDao.findByApiSeqAndEntSeq(newApi.getSeq(), entSeq);
            SApiExt importApiExt = apiExtDao.findByApiSeqAndEntSeq(importApi.getSeq(), entSeq);
            if (ObjectUtils.isNotEmpty(newApiExt)) {
                if (ObjectUtils.isNotEmpty(importApiExt)) {
                    newApiExt.setApiMock(importApiExt.getApiMock());
                } else {
                    newApiExt.setApiMock(null);
                }
                apiExtDao.save(newApiExt);
            } else {
                if (ObjectUtils.isNotEmpty(importApiExt)) {
                    newApiExt = new SApiExt();
                    BeanUtils.copyProperties(importApiExt, newApiExt);
                    newApiExt.setApiSeq(newApi.getSeq());
                    apiExtDao.save(newApiExt);
                }
            }
        });


        return new ResponseResult("success");
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
    public void setServiceTagDao(SApiServiceTagDao serviceTagDao) {
        this.serviceTagDao = serviceTagDao;
    }
}
