package com.hell.action.account.dept;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptDao;
import com.hell.db.table.provider.SDept;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DeptRequest;
import com.hell.dto.response.DeptResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@Api(tags = "dept api")
public class UpdateDeptAction implements BaseAction<DeptRequest, DeptResponse> {

    private SDeptDao deptDao;

    @PutMapping(path = "/api/t1/account/updateDept")
    @ApiOperation("团队信息查询")
    public DeptResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody DeptRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();
        SDept d = deptDao.findByDeptNameAndEntSeq(request.getDeptName(),entSeq);
        if ( null != d && (d.getSeq()).compareTo(request.getSeq()) != 0){
            throw new ValidationException(CheckMsg.VALIDATION_DEPT_NAME_NOT_SAME);
        }

        SDept dept = deptDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        dept.setDeptName(request.getDeptName());
        dept.setInfo(request.getInfo());
        dept = deptDao.save(dept);

        DeptResponse r = new DeptResponse();
        r.setDept(dept);
        return r;
    }

    @Autowired
    public void setDeptDao(SDeptDao deptDao) {
        this.deptDao = deptDao;
    }
}
