package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiGroupDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.QryApiRequest;
import com.hell.dto.response.ApisResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "api")
public class QryApiAction implements BaseAction<QryApiRequest, ApisResponse> {

    private SApiDao apiDao;
    private SApiGroupDao apiGroupDao;

    @GetMapping(path = "/api/t1/service/qryApi")
    @ApiOperation("API查询")
    @Override
    public ApisResponse execute(HttpServletRequest httpServletRequest, @Valid QryApiRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<SApi> list = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(request.getGroupSeq())) {
            List<Integer> groupSeqList = new ArrayList<>();
            groupSeqList.add(request.getGroupSeq());
            List<SApiGroup> s = apiGroupDao.findByParentSeqAndEntSeq(request.getGroupSeq(), entSeq);
            s.forEach(apiGroup -> {
                groupSeqList.add(apiGroup.getSeq());
            });

            list = apiDao.findByGroupSeqInAndEntSeqAndAppEnvSeq(groupSeqList, entSeq, request.getAppEnvSeq(),Sort.by(Sort.Direction.DESC, "seq"));
        } else {
            list = apiDao.findByEntSeqAndAppEnvSeq(entSeq,
                    request.getAppEnvSeq(),
                    Sort.by(Sort.Direction.DESC, "seq"));
        }

        ApisResponse r = new ApisResponse();
        r.setApis(list);

        return r;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }
}
