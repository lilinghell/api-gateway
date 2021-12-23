package com.hell.action.test.plan;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestCasesDao;
import com.hell.dao.STestPlanCasesDao;
import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.STestPlanCases;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.AddTestPlanCasesRequest;
import com.hell.dto.response.test.TestCasesResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
public class AddTestPlanCasesAction implements BaseAction<AddTestPlanCasesRequest, TestCasesResponse> {
    private STestPlanCasesDao testPlanCasesDao;
    private STestCasesDao testCasesDao;

    @PostMapping(path = "/api/t1/test/addTestPlanCases")
    @ApiOperation("测试用例计划")
    @Override
    public TestCasesResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddTestPlanCasesRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<Integer> caseList = request.getCaseSeqList();
        List<STestPlanCases> saveList = new ArrayList<>();
        caseList.forEach(testCaseSeq -> {
            STestPlanCases planCases = new STestPlanCases();
            planCases.setAppSeq(request.getAppSeq());
            planCases.setTestCaseSeq(testCaseSeq);
            planCases.setEntSeq(entSeq);
            planCases.setTestPlanSeq(request.getTestPlanSeq());
            saveList.add(planCases);
        });

        testPlanCasesDao.saveAll(saveList);

        List<STestCases> re = testCasesDao.findBySeqInAndEntSeq(request.getCaseSeqList(), entSeq);

        TestCasesResponse r = new TestCasesResponse();
        r.setCases(re);
        return r;
    }

    @Autowired
    public void setTestPlanCasesDao(STestPlanCasesDao testPlanCasesDao) {
        this.testPlanCasesDao = testPlanCasesDao;
    }

    @Autowired
    public void setTestCasesDao(STestCasesDao testCasesDao) {
        this.testCasesDao = testCasesDao;
    }
}
