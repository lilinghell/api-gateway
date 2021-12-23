package com.hell.action.account.dept;

import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptMemberDao;
import com.hell.db.table.provider.SDeptUser;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.AddDeptMemberRequest;
import com.hell.dto.response.DeptMemberResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "dept api")
public class AddDeptMemberAction implements BaseAction<AddDeptMemberRequest, DeptMemberResponse> {

    private SDeptMemberDao deptMemberDao;
    private QryDeptMemberAction qryDeptMember;

    @PostMapping(path = "/api/t1/account/addDeptMember")
    @ApiOperation("部门成员添加")
    public DeptMemberResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddDeptMemberRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();

        SDeptUser m = new SDeptUser();
        m.setMemberSeq(request.getMemberSeq());
        m.setDeptSeq(request.getDeptSeq());

        deptMemberDao.saveAndFlush(m);

        List<SUser> rList = qryDeptMember.qryDeptMemberInfo(entSeq, request.getDeptSeq());

        DeptMemberResponse r = new DeptMemberResponse();
        r.setMembers(rList);
        return r;
    }

    @Autowired
    public void setDeptMemberDao(SDeptMemberDao deptMemberDao) {
        this.deptMemberDao = deptMemberDao;
    }

    @Autowired
    public void setQryDeptMember(QryDeptMemberAction qryDeptMember) {
        this.qryDeptMember = qryDeptMember;
    }
}
