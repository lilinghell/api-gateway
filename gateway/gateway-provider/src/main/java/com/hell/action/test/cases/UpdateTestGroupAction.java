package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestGroupDao;
import com.hell.db.table.provider.STestGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.UpdateTestGroupRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class UpdateTestGroupAction implements BaseAction<UpdateTestGroupRequest, ResponseResult> {
    private STestGroupDao testGroupDao;

    @PutMapping(path = "/api/t1/test/updateTestGroup")
    @ApiOperation("测试集合修改")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateTestGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        STestGroup testGroup = testGroupDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        testGroup.setInfo(request.getInfo());
        testGroup.setName(request.getName());
        testGroup = testGroupDao.save(testGroup);
        return new ResponseResult(testGroup);
    }

    @Autowired
    public void setTestGroupDao(STestGroupDao testGroupDao) {
        this.testGroupDao = testGroupDao;
    }
}
