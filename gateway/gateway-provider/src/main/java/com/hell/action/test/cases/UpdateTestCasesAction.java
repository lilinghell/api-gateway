package com.hell.action.test.cases;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.STestCasesDao;
import com.hell.dao.STestRunDao;
import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.UpdateTestCasesRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class UpdateTestCasesAction implements BaseAction<UpdateTestCasesRequest, ResponseResult> {

    private STestCasesDao testCasesDao;
    private STestRunDao testRunDao;

    @Transactional(rollbackFor = Exception.class)
    @PutMapping(path = "/api/t1/test/updateTestCases")
    @ApiOperation("测试用例修改")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateTestCasesRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        STestCases testCase = testCasesDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        BeanUtils.copyProperties(request, testCase);
        testCase.setStatus(Dictionary.CASE_STATUS_2);
        testCase = testCasesDao.save(testCase);

        testRunDao.deleteByCaseSeqAndEntSeq(request.getSeq(), entSeq);

        return new ResponseResult(testCase);
    }

    @Autowired
    public void setTestCasesDao(STestCasesDao testCasesDao) {
        this.testCasesDao = testCasesDao;
    }

    @Autowired
    public void setTestRunDao(STestRunDao testRunDao) {
        this.testRunDao = testRunDao;
    }
}
