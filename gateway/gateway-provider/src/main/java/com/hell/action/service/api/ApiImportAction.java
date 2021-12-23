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
import com.hell.dto.request.ApiImportRequest;
import com.hell.entity.api.ApiExportInfo;
import com.hell.entity.api.ApiInfo;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@Api(tags = "api")
public class ApiImportAction implements BaseAction<ApiImportRequest, ResponseResult> {

    private SApiDao apiDao;
    private SApiExtDao apiExtDao;
    private SApiGroupDao apiGroupDao;

    @PostMapping(path = "/api/t1/service/apiImport")
    @ApiOperation("API导出信息")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody ApiImportRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        ApiExportInfo info = request.getApiExportInfo();
        Map<Integer, Integer> newApiGroupSeq = new HashMap<>();
        if (request.getApiGroupFlg()) {
            //导入组信息
            List<SApiGroup> apiGroupList = info.getApiGroupList();
            List<SApiGroup> rootApiGroups = apiGroupList.stream().filter(apiGroup ->
                    ObjectUtils.isEmpty(apiGroup.getParent())).collect(Collectors.toList());

            rootApiGroups.forEach(importApiGroup -> {

                SApiGroup newGroup = saveApiGroup(newApiGroupSeq, importApiGroup, entSeq);

                List<SApiGroup> subApiGroups = apiGroupList.stream().filter(apiGroup ->
                        ObjectUtils.isNotEmpty(apiGroup.getParent()) &&
                                apiGroup.getParent().getSeq().compareTo(importApiGroup.getSeq()) == 0)
                        .map(apiGroup -> {
                            apiGroup.setParent(newGroup);
                            return apiGroup;
                        })
                        .collect(Collectors.toList());

                subApiGroups.forEach(importSubApiGroup -> {
                    SApiGroup newSubGroup = saveApiGroup(newApiGroupSeq, importSubApiGroup, entSeq);
                });
            });
        }
        List<ApiInfo> apiInfoList = info.getApiInfoList();
        apiInfoList.forEach(apiInfo -> {
            SApi api = apiInfo.getApi();
            api.setEntSeq(entSeq);
            api.setSyncGateway(Dictionary.API_SYNC_GATEWAY_1);
            SApiExt apiExt = apiInfo.getApiExt();

            SApi oldApi = apiDao.findByUrlAndEntSeqAndAppEnvSeq(api.getUrl(),
                    entSeq, request.getAppEnvSeq());
            if (ObjectUtils.isNotEmpty(oldApi)) {
                //有则更新
                api.setSeq(oldApi.getSeq());
            } else {
                api.setSeq(null);
            }
            SApiGroup group = apiGroupDao.findBySeqAndEntSeq(
                    newApiGroupSeq.get(apiInfo.getApiGroupKey()), entSeq);
            api.setGroup(group);
            api.setAppEnvSeq(request.getAppEnvSeq());

            SApi newApi = apiDao.save(api);

            if (ObjectUtils.isNotEmpty(apiExt)) {
                apiExt.setEntSeq(entSeq);
                apiExt.setApiSeq(newApi.getSeq());
                apiExtDao.save(apiExt);
            }
        });

        return new ResponseResult("sucess");
    }

    private SApiGroup saveApiGroup(Map<Integer, Integer> newApiGroupSeq, SApiGroup importApiGroup, Integer entSeq) {
        SApiGroup newGroup = new SApiGroup();
        SApiGroup oldGroup = apiGroupDao.findByNameAndEntSeqAndAppSeq(importApiGroup.getName(),
                entSeq, importApiGroup.getAppSeq());
        if (ObjectUtils.isNotEmpty(oldGroup)) {
            //存在则更新
            newApiGroupSeq.put(importApiGroup.getSeq(), oldGroup.getSeq());
            oldGroup.setInfo(importApiGroup.getInfo());
            newGroup = apiGroupDao.save(oldGroup);
        } else {
            BeanUtils.copyProperties(importApiGroup, newGroup);
            newGroup.setSeq(null);
            newGroup.setEntSeq(entSeq);
            newGroup = apiGroupDao.save(newGroup);
            newApiGroupSeq.put(importApiGroup.getSeq(), newGroup.getSeq());
        }
        return newGroup;
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
