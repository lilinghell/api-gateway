package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestGroupDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.DelTestGroupRequest;
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
public class DelTestGroupAction implements BaseAction<DelTestGroupRequest, ResponseResult> {
    private STestGroupDao testGroupDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/test/delTestGroup/{seq}")
    @ApiOperation("测试集合删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelTestGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        testGroupDao.deleteBySeqAndEntSeq(request.getSeq(), entSeq);
        return new ResponseResult("success");
    }

    @Autowired
    public void setTestGroupDao(STestGroupDao testGroupDao) {
        this.testGroupDao = testGroupDao;
    }
}
