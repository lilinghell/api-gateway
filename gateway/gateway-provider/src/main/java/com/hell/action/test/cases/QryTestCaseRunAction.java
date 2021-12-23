package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestRunDao;
import com.hell.db.table.provider.STestRun;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.QryTestCasesRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "test")
public class QryTestCaseRunAction implements BaseAction<QryTestCasesRequest, ResponseResult> {

    private STestRunDao testRunDao;

    @PostMapping(path = "/api/t1/test/qryTestCaseRun")
    @ApiOperation("测试用例执行结果查询")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody QryTestCasesRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<STestRun> testRuns = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(request.getCaseSeqList())) {
            testRuns = testRunDao.findByCaseSeqInAndEntSeq(request.getCaseSeqList(), entSeq);
        }
        return new ResponseResult(testRuns);
    }

    @Autowired
    public void setTestRunDao(STestRunDao testRunDao) {
        this.testRunDao = testRunDao;
    }
}
