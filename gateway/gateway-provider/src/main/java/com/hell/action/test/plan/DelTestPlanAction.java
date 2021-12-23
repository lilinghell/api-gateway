package com.hell.action.test.plan;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.STestPlanCasesDao;
import com.hell.dao.STestPlanDao;
import com.hell.db.table.provider.STestPlanCases;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.test.DelTestPlanRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "test")
public class DelTestPlanAction implements BaseAction<DelTestPlanRequest, ResponseResult> {
    private STestPlanCasesDao sTestPlanCasesDao;
    private STestPlanDao sTestPlanDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/test/delTestPlan/{seq}")
    @ApiOperation("测试用例计划删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelTestPlanRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<STestPlanCases> sTestPlanCases = sTestPlanCasesDao.findByTestPlanSeqAndEntSeq(request.getSeq(), entSeq);
        if (ObjectUtils.isNotEmpty(sTestPlanCases)) {
            throw new ValidationException(CheckMsg.VALIDATION_DO_NOT_DEL);
        }
        sTestPlanDao.deleteBySeqAndEntSeq(request.getSeq(), entSeq);
        return new ResponseResult("success");
    }

    @Autowired
    public void setsTestPlanCasesDao(STestPlanCasesDao sTestPlanCasesDao) {
        this.sTestPlanCasesDao = sTestPlanCasesDao;
    }

    @Autowired
    public void setsTestPlanDao(STestPlanDao sTestPlanDao) {
        this.sTestPlanDao = sTestPlanDao;
    }
}
