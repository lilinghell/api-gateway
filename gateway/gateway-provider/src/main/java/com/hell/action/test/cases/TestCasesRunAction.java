package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.TestRunRequest;
import com.hell.service.TestCaseRunService;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class TestCasesRunAction implements BaseAction<TestRunRequest, ResponseResult> {
    private TestCaseRunService testCaseRunService;
    private SAppEnvDao appEnvDao;

    @PostMapping(path = "/api/t1/test/testRun")
    @ApiOperation("测试用例执行")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody TestRunRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        testCaseRunService.testRun(request.getCaseSeqList(), entSeq, request.getAppSeq());
        return new ResponseResult("success");
    }
    
    @Autowired
    public void setTestCaseRunService(TestCaseRunService testCaseRunService) {
        this.testCaseRunService = testCaseRunService;
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
