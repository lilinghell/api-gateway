package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiGroupDao;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SAppEnv;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ApiExtRequest;
import com.hell.entity.api.ApiTable;
import com.hell.service.ApiTableServiceImpl;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "api")
public class QryApiPreviewAction implements BaseAction<ApiExtRequest, ResponseResult> {

    private ApiTableServiceImpl apiTableService;
    private SApiDao apiDao;
    private SApiGroupDao apiGroupDao;
    private SAppEnvDao appEnvDao;

    @GetMapping(path = "/api/t1/service/qryApiPreview")
    @ApiOperation("API预览")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid ApiExtRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<List<ApiTable>> re = new ArrayList<>();
        SAppEnv appEnv = appEnvDao.findBySeqAndEntSeq(request.getAppEnvSeq(), entSeq).get(0);
        if (ObjectUtils.isNotEmpty(request.getApiSeq())) {
            //单个API预览
            SApi api = apiDao.findBySeqAndEntSeq(request.getApiSeq(), entSeq);
            List<ApiTable> list = apiTableService.getApiTableList(api, appEnv);
            re.add(list);
        } else if (ObjectUtils.isNotEmpty(request.getGroupSeq())) {
            //组下API的预览
            List<SApi> apis = null;
            if (request.getGroupSeq() == -1) {
                apis = apiDao.findByEntSeqAndAppEnvSeq(entSeq, request.getAppEnvSeq(),
                        Sort.by(Sort.Direction.DESC, "seq"));
            } else {
                List<Integer> groupSeqList = new ArrayList<>();
                groupSeqList.add(request.getGroupSeq());
                List<SApiGroup> s = apiGroupDao.findByParentSeqAndEntSeq(request.getGroupSeq(), entSeq);
                s.forEach(apiGroup -> {
                    groupSeqList.add(apiGroup.getSeq());
                });

                apis = apiDao.findByGroupSeqInAndEntSeqAndAppEnvSeq(groupSeqList, entSeq,
                        request.getAppEnvSeq(),
                        Sort.by(Sort.Direction.DESC, "seq"));
            }
            apis = apis.stream().filter(api -> StringUtils.isNotEmpty(api.getUrl())).collect(Collectors.toList());
            for (SApi api : apis) {
                List<ApiTable> list = apiTableService.getApiTableList(api, appEnv);
                re.add(list);
            }
        }
        return new ResponseResult(re);
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
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
    public void setApiTableService(ApiTableServiceImpl apiTableService) {
        this.apiTableService = apiTableService;
    }
}
