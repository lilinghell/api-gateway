package com.hell.action.service.api;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiGroupDao;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.AddApiGroupRequest;
import com.hell.dto.response.ApiGroupResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "api")
public class AddApiGroupAction implements BaseAction<AddApiGroupRequest, ApiGroupResponse> {

    private SApiGroupDao apiGroupDao;

    @PostMapping(path = "/api/t1/service/addApiGroup")
    @ApiOperation("API组添加")
    @Override
    public ApiGroupResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddApiGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApiGroup group = apiGroupDao.findByNameAndEntSeqAndAppSeq(request.getName(), entSeq, request.getAppSeq());
        if (ObjectUtils.isNotEmpty(group)) {
            throw new ValidationException(CheckMsg.VALIDATION_NAME_EXIST);
        }
        SApiGroup apiGroup = new SApiGroup();
        apiGroup.setEntSeq(entSeq);
        apiGroup.setName(request.getName());
        apiGroup.setParent(request.getParent());
        apiGroup.setAppSeq(request.getAppSeq());

        apiGroup = apiGroupDao.save(apiGroup);

        ApiGroupResponse r = new ApiGroupResponse();
        r.setApiGroup(apiGroup);
        return r;
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }
}
