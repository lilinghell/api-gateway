package com.hell.action.test.parameter;

import com.hell.common.ServiceUtils;
import com.hell.dao.SParameterDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ParameterRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class DelParameterAction implements BaseAction<ParameterRequest, ResponseResult> {
    private SParameterDao parameterDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/test/delParameter/{appSeq}/{seq}")
    @ApiOperation("参数变量删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid ParameterRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        //todo 判断删除条件

        parameterDao.deleteBySeqAndEntSeq(request.getSeq(), entSeq);

        return new ResponseResult("success");
    }

    @Autowired
    public void setParameterDao(SParameterDao parameterDao) {
        this.parameterDao = parameterDao;
    }
}
