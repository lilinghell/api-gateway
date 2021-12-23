package com.hell.action.service.api;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiGroupDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DelApiGroupRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "api")
public class DelApiGroupAction implements BaseAction<DelApiGroupRequest, ResponseResult> {

    private SApiGroupDao apiGroupDao;
    private SApiDao apiDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/service/delApiGroup/{appEnvSeq}/{seq}")
    @ApiOperation("API删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelApiGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SApi> r = apiDao.findByGroupSeqAndEntSeqAndAppEnvSeq(request.getSeq(), entSeq, request.getAppEnvSeq());
        if (ObjectUtils.isNotEmpty(r)) {
            throw new ValidationException(CheckMsg.VALIDATION_API_GROUP_NOT_DEL);
        }
        List<SApiGroup> f = apiGroupDao.findByParentSeqAndEntSeq(request.getSeq(), entSeq);
        if (ObjectUtils.isNotEmpty(f)) {
            throw new ValidationException(CheckMsg.VALIDATION_API_GROUP_NOT_DEL);
        }

        apiGroupDao.deleteBySeqAndEntSeq(request.getSeq(), entSeq);

        return new ResponseResult("success");
    }

    @Autowired
    public void setApiGroupDao(SApiGroupDao apiGroupDao) {
        this.apiGroupDao = apiGroupDao;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }
}
