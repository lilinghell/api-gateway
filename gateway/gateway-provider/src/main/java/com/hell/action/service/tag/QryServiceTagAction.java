package com.hell.action.service.tag;

import com.hell.common.ServiceUtils;
import com.hell.dao.SServiceTagDao;
import com.hell.db.table.provider.SServiceTag;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.tag.QryServiceTagRequest;
import com.hell.dto.response.tag.ServiceTagResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = "tag")
@RestController
public class QryServiceTagAction implements BaseAction<QryServiceTagRequest, ServiceTagResponse> {

    private SServiceTagDao serviceTagDao;

    @GetMapping(path = "/api/t1/service/qryServiceTag")
    @ApiOperation("环境查询")
    @Override
    public ServiceTagResponse execute(HttpServletRequest httpServletRequest, QryServiceTagRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SServiceTag> list = serviceTagDao.findByAppSeqAndEntSeq(request.getAppSeq(), entSeq, Sort.by(Sort.Direction.DESC,"seq"));

        ServiceTagResponse r = new ServiceTagResponse();
        r.setServiceTags(list);
        return r;
    }

    @Autowired
    public void setServiceTagDao(SServiceTagDao serviceTagDao) {
        this.serviceTagDao = serviceTagDao;
    }
}
