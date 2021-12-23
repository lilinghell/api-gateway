package com.hell.action.service.tag;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiServiceTagDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.tag.QryApiByTagRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Api(tags = "tag")
@RestController
public class QryApiByTagAction implements BaseAction<QryApiByTagRequest, ResponseResult> {

    private SApiServiceTagDao apiServiceTagDao;

    @GetMapping(path = "/api/t1/service/qryApiByTag")
    @ApiOperation("API TAG查询")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, QryApiByTagRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<Map<String, Object>> re = apiServiceTagDao.QryByTagSeqAndEntSeq(request.getServiceTagSeq(), entSeq);
        return new ResponseResult(re);
    }

    @Autowired
    public void setApiServiceTagDao(SApiServiceTagDao apiServiceTagDao) {
        this.apiServiceTagDao = apiServiceTagDao;
    }
}
