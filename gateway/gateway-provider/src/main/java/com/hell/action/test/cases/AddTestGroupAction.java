package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestGroupDao;
import com.hell.db.table.provider.STestGroup;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.AddTestGroupRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class AddTestGroupAction implements BaseAction<AddTestGroupRequest, ResponseResult> {
    private STestGroupDao testGroupDao;

    @PostMapping(path = "/api/t1/test/addTestGroup")
    @ApiOperation("测试集合添加")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddTestGroupRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        STestGroup s = new STestGroup();
        BeanUtils.copyProperties(request, s);
        s.setEntSeq(entSeq);
        if (ObjectUtils.isNotEmpty(request.getParentSeq())) {
            STestGroup parent = testGroupDao.findBySeqAndEntSeq(request.getParentSeq(), entSeq);
            s.setParent(parent);
        }

        s = testGroupDao.save(s);
        return new ResponseResult(s);
    }

    @Autowired
    public void setTestGroupDao(STestGroupDao testGroupDao) {
        this.testGroupDao = testGroupDao;
    }
}
