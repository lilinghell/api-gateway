package com.hell.action.test.plan;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestPlanDao;
import com.hell.db.table.provider.STestPlan;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.AddTestPlanRequest;
import com.hell.dto.response.test.TestPlanResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.common.utils.Utils;
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
public class AddTestPlanAction implements BaseAction<AddTestPlanRequest, TestPlanResponse> {
    private STestPlanDao testPlanDao;

    @PostMapping(path = "/api/t1/test/addTestPlan")
    @ApiOperation("测试用例计划")
    @Override
    public TestPlanResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddTestPlanRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        STestPlan testPlan = new STestPlan();
        testPlan.setAppSeq(request.getAppSeq());
        testPlan.setName(request.getName());
        testPlan.setPlanInfo(request.getPlanInfo());
        testPlan.setPlanTime(Utils.objectToJson(request.getPlanTime()));
        testPlan.setPlanRule(Utils.objectToJson(request.getPlanRule()));
        testPlan.setEntSeq(entSeq);
        testPlan.setUsingFlg(request.getUsingFlg());

        testPlan = testPlanDao.save(testPlan);

        TestPlanResponse r = new TestPlanResponse();
        List<STestPlan> testPlanList = new ArrayList<>();
        testPlanList.add(testPlan);
        r.setTestPlans(testPlanList);
        return r;
    }

    @Autowired
    public void setTestPlanDao(STestPlanDao testPlanDao) {
        this.testPlanDao = testPlanDao;
    }
}
