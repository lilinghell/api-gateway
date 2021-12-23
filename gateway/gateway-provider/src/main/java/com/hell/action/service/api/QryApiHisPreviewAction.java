package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiHisDao;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiHis;
import com.hell.db.table.provider.SAppEnv;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ApiHisRequest;
import com.hell.entity.api.ApiTable;
import com.hell.service.ApiTableServiceImpl;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "api")
public class QryApiHisPreviewAction implements BaseAction<ApiHisRequest, ResponseResult> {

    private ApiTableServiceImpl apiTableService;
    private SApiHisDao apiHisDao;
    private SAppEnvDao appEnvDao;

    @GetMapping(path = "/api/t1/service/qryApiHisPreview")
    @ApiOperation("API预览")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid ApiHisRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<List<ApiTable>> re = new ArrayList<>();

        SApiHis apiHis = apiHisDao.findByIdAndEntSeq(request.getId(), entSeq);
        SApi api = new SApi();
        BeanUtils.copyProperties(apiHis, api);

        SAppEnv appEnv = appEnvDao.findBySeqAndEntSeq(request.getAppEnvSeq(), entSeq).get(0);
        List<ApiTable> list = apiTableService.getApiTableList(api, appEnv);
        re.add(list);
        return new ResponseResult(re);
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }

    @Autowired
    public void setApiHisDao(SApiHisDao apiHisDao) {
        this.apiHisDao = apiHisDao;
    }

    @Autowired
    public void setApiTableService(ApiTableServiceImpl apiTableService) {
        this.apiTableService = apiTableService;
    }
}
