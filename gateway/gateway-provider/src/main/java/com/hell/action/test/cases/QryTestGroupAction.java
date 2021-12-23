package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestGroupDao;
import com.hell.db.table.provider.STestGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.TestGroupRequest;
import com.hell.dto.response.test.TestGroupResponse;
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
public class QryTestGroupAction implements BaseAction<TestGroupRequest, TestGroupResponse> {
    private STestGroupDao testGroupDao;

    @GetMapping(path = "/api/t1/test/qryTestGroup")
    @ApiOperation("测试集合查询")
    @Override
    public TestGroupResponse execute(HttpServletRequest httpServletRequest, TestGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<STestGroup> list = testGroupDao.findByEntSeqAndAppSeq(entSeq, request.getAppSeq());

        TestGroupResponse re = new TestGroupResponse();

        re.setTestGroups(list);
        return re;
    }

    @Autowired
    public void setTestGroupDao(STestGroupDao testGroupDao) {
        this.testGroupDao = testGroupDao;
    }
}
