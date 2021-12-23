package com.hell.action.test.plan;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestPlanCasesDao;
import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.QryTestPlanRequest;
import com.hell.dto.response.test.TestCasesResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = "test")
public class QryTestPlanCasesAction implements BaseAction<QryTestPlanRequest, TestCasesResponse> {

    private STestPlanCasesDao testPlanCasesDao;

    @GetMapping(path = "/api/t1/test/qryTestPlanCases")
    @ApiOperation("测试计划用例查询")
    @Override
    public TestCasesResponse execute(HttpServletRequest httpServletRequest, QryTestPlanRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<STestCases> list = testPlanCasesDao.qryTestPlanCases(entSeq, request.getTestPlanSeq(), request.getAppSeq());

        TestCasesResponse re = new TestCasesResponse();
        re.setCases(list);
        return re;
    }

    @Autowired
    public void setTestPlanCasesDao(STestPlanCasesDao testPlanCasesDao) {
        this.testPlanCasesDao = testPlanCasesDao;
    }
}
