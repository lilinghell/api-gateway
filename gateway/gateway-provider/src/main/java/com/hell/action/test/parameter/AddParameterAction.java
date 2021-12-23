package com.hell.action.test.parameter;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SParameterDao;
import com.hell.db.table.provider.SParameter;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.AddParameterRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class AddParameterAction implements BaseAction<AddParameterRequest, ResponseResult> {
    private SParameterDao parameterDao;

    @PostMapping(path = "/api/t1/test/addParameter")
    @ApiOperation("参数变量添加")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddParameterRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SParameter sParameter = parameterDao.findByNameAndAppSeqAndEntSeq(request.getName(),
                request.getAppSeq(), entSeq);
        if (ObjectUtils.isNotEmpty(sParameter)) {
            throw new ValidationException(CheckMsg.VALIDATION_NAME_EXIST);
        }
        SParameter sParameter2 = parameterDao.findByEnNameAndAppSeqAndEntSeq(request.getEnName(),
                request.getAppSeq(), entSeq);
        if (ObjectUtils.isNotEmpty(sParameter2)) {
            throw new ValidationException(CheckMsg.VALIDATION_ENAME_EXIST);
        }

        SParameter s = new SParameter();
        BeanUtils.copyProperties(request, s);
        s.setEntSeq(entSeq);

        s = parameterDao.save(s);
        return new ResponseResult(s);
    }

    @Autowired
    public void setParameterDao(SParameterDao parameterDao) {
        this.parameterDao = parameterDao;
    }
}
