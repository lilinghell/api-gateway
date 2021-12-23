package com.hell.action.account.dept;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptDao;
import com.hell.dao.SDeptMemberDao;
import com.hell.db.table.provider.SDept;
import com.hell.db.table.provider.SDeptUser;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DelDeptRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "dept api")
@RestController
public class DelDeptAction implements BaseAction<DelDeptRequest, ResponseResult> {

    private SDeptDao deptDao;
    private SDeptMemberDao deptMemberDao;

    @DeleteMapping(path = "/api/t1/account/delDept/{seq}")
    @ApiOperation("部门删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelDeptRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();

        SDept dept = deptDao.findById(request.getSeq()).get();
        if (dept.getEntSeq() != entSeq) {
            throw new ValidationException(CheckMsg.VALIDATION_UNAUTH_OPERATION);
        }
        List<SDeptUser> list = deptMemberDao.findByDeptSeq(request.getSeq());
        if(ObjectUtils.isNotEmpty(list)) {
            throw new ValidationException(CheckMsg.VALIDATION_MEMBER_NOT_NULL);
        }

        deptDao.deleteById(request.getSeq());
        return new ResponseResult("success");
    }

    @Autowired
    public void setDeptDao(SDeptDao deptDao) {
        this.deptDao = deptDao;
    }

    @Autowired
    public void setDeptMemberDao(SDeptMemberDao deptMemberDao) {
        this.deptMemberDao = deptMemberDao;
    }
}
