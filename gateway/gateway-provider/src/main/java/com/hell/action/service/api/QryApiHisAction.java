package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiHisDao;
import com.hell.db.table.provider.SApiHis;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ApiExtRequest;
import com.hell.dto.response.ApiHisResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = "api")
public class QryApiHisAction implements BaseAction<ApiExtRequest, ApiHisResponse> {
    private SApiHisDao apiHisDao;

    @GetMapping(path = "/api/t1/service/qryApiHis")
    @ApiOperation("API历史版本查询")
    @Override
    public ApiHisResponse execute(HttpServletRequest httpServletRequest, ApiExtRequest request) throws Exception {

        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SApiHis> list = apiHisDao.findBySeqAndEntSeq(request.getApiSeq(), entSeq, Sort.by(Sort.Direction.DESC, "id"));

        ApiHisResponse r = new ApiHisResponse();
        r.setApis(list);

        return r;
    }

    @Autowired
    public void setApiHisDao(SApiHisDao apiHisDao) {
        this.apiHisDao = apiHisDao;
    }
}
