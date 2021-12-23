package com.hell.action.test.plan;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestPlanDao;
import com.hell.db.table.provider.STestPlan;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.QryTestPlanRequest;
import com.hell.dto.response.test.TestPlanResponse;
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
public class QryTestPlanAction implements BaseAction<QryTestPlanRequest, TestPlanResponse> {

    private STestPlanDao testPlanDao;

    @GetMapping(path = "/api/t1/test/qryTestPlan")
    @ApiOperation("测试计划查询")
    @Override
    public TestPlanResponse execute(HttpServletRequest httpServletRequest, QryTestPlanRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<STestPlan> list = testPlanDao.findByAppSeqAndEntSeq(request.getAppSeq(), entSeq);

        TestPlanResponse re = new TestPlanResponse();
        re.setTestPlans(list);
        return re;
    }

    @Autowired
    public void setTestPlanDao(STestPlanDao testPlanDao) {
        this.testPlanDao = testPlanDao;
    }
}
