package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiExtDao;
import com.hell.db.table.provider.SApiExt;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ApiExtRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "api")
public class QryApiExtAction implements BaseAction<ApiExtRequest, ResponseResult> {

    private SApiExtDao apiExtDao;

    @GetMapping(path = "/api/t1/service/qryApiExt")
    @ApiOperation("API扩展查询")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid ApiExtRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApiExt e = apiExtDao.findByApiSeqAndEntSeq(request.getApiSeq(), entSeq);
        return new ResponseResult(e);
    }

    @Autowired
    public void setApiExtDao(SApiExtDao apiExtDao) {
        this.apiExtDao = apiExtDao;
    }
}
