package com.hell.action.service.tag;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiServiceTagDao;
import com.hell.db.table.provider.SApiServiceTag;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.tag.QryApiServiceTagRequest;
import com.hell.dto.response.tag.ApiServiceTagResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "tag")
@RestController
public class QryApiServiceTagAction implements BaseAction<QryApiServiceTagRequest, ApiServiceTagResponse> {

    private SApiServiceTagDao apiServiceTagDao;

    @GetMapping(path = "/api/t1/service/qryApiServiceTag")
    @ApiOperation("API TAG查询")
    @Override
    public ApiServiceTagResponse execute(HttpServletRequest httpServletRequest, QryApiServiceTagRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<SApiServiceTag> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(request.getApiSeq())) {
            list = apiServiceTagDao.findByApiSeqAndEntSeq(request.getApiSeq(), entSeq);
        }
        if (ObjectUtils.isNotEmpty(request.getServiceTagSeq())) {
            list = apiServiceTagDao.findByServiceTagSeqAndEntSeq(request.getServiceTagSeq(), entSeq);
        }

        ApiServiceTagResponse r = new ApiServiceTagResponse();
        r.setApiServiceTags(list);
        return r;
    }

    @Autowired
    public void setApiServiceTagDao(SApiServiceTagDao apiServiceTagDao) {
        this.apiServiceTagDao = apiServiceTagDao;
    }
}
