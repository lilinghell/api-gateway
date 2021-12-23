package com.hell.action.test.plan;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestPlanCasesDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.DelTestPlanCasesRequest;
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
public class DelTestPlanCasesAction implements BaseAction<DelTestPlanCasesRequest, ResponseResult> {
    private STestPlanCasesDao sTestPlanCasesDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/test/delTestPlanCases/{seq}")
    @ApiOperation("计划测试用例删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelTestPlanCasesRequest request) throws Exception{
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        sTestPlanCasesDao.deleteByTestCaseSeqAndEntSeq(request.getSeq(),entSeq);
        return new ResponseResult("success");
    }
    @Autowired
    public void setsTestPlanCasesDao(STestPlanCasesDao sTestPlanCasesDao) {
        this.sTestPlanCasesDao = sTestPlanCasesDao;
    }
}
