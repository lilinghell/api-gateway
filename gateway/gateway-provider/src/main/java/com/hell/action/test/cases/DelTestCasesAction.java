package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestCasesDao;
import com.hell.dao.STestPlanCasesDao;
import com.hell.dao.STestRunDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.DelTestCasesRequest;
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
public class DelTestCasesAction implements BaseAction<DelTestCasesRequest, ResponseResult> {
    private STestCasesDao testCasesDao;
    private STestPlanCasesDao sTestPlanCasesDao;
    private STestRunDao sTestRunDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/test/delTest/{seq}")
    @ApiOperation("测试用例删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid  DelTestCasesRequest request) throws Exception{
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        sTestPlanCasesDao.deleteByTestCaseSeqAndEntSeq(request.getSeq(),entSeq);
        testCasesDao.deleteBySeqAndEntSeq(request.getSeq(), entSeq);
        return new ResponseResult("success");
    }

    @Autowired
    public void setTestCasesDao(STestCasesDao testCasesDao) {
        this.testCasesDao = testCasesDao;
    }
    @Autowired
    public void setsTestPlanCasesDao(STestPlanCasesDao sTestPlanCasesDao) {
        this.sTestPlanCasesDao = sTestPlanCasesDao;
    }
    @Autowired
    public void setsTestRunDao(STestRunDao sTestRunDao) {
        this.sTestRunDao = sTestRunDao;
    }
}
