package com.hell.action.test.cases;

import com.hell.common.ServiceUtils;
import com.hell.dao.STestCasesDao;
import com.hell.db.table.provider.STestCases;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.AddTestCasesRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "test")
public class AddTestCasesAction implements BaseAction<AddTestCasesRequest, ResponseResult> {

    private STestCasesDao testCasesDao;

    @PostMapping(path = "/api/t1/test/addTestCases")
    @ApiOperation("测试用例添加")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddTestCasesRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        STestCases cases = new STestCases();
        BeanUtils.copyProperties(request, cases);
        cases.setEntSeq(entSeq);
        cases = testCasesDao.saveAndFlush(cases);

        cases.setUniKey(cases.getSeq());
        testCasesDao.save(cases);

        return new ResponseResult(cases);
    }

    @Autowired
    public void setTestCasesDao(STestCasesDao testCasesDao) {
        this.testCasesDao = testCasesDao;
    }
}
