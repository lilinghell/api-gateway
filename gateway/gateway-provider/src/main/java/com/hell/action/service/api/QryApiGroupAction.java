package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiGroupDao;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.QryApiGroupRequest;
import com.hell.dto.response.ApiGroupsResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "api")
public class QryApiGroupAction implements BaseAction<QryApiGroupRequest, ApiGroupsResponse> {

    private SApiGroupDao apiGroupDao;

    @GetMapping(path = "/api/t1/service/qryApiGroup")
    @ApiOperation("API组查询")
    @Override
    public ApiGroupsResponse execute(HttpServletRequest httpServletRequest, @Valid QryApiGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SApiGroup> list = apiGroupDao.findByEntSeqAndAppSeq(entSeq, request.getAppSeq());

        ApiGroupsResponse r = new ApiGroupsResponse();
        r.setApiGroups(list);

        return r;
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }
}
