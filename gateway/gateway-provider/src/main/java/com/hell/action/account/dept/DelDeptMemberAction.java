package com.hell.action.account.dept;

import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptMemberDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DelDeptMemberRequest;
import com.hell.dto.response.DeptMemberResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "dept api")
@RestController
public class DelDeptMemberAction implements BaseAction<DelDeptMemberRequest, DeptMemberResponse> {

    private SDeptMemberDao deptMemberDao;
    private QryDeptMemberAction qryDeptMember;

    @Transactional(rollbackFor = Exception.class)
    @PutMapping(path = "/api/t1/account/delDeptMember")
    @ApiOperation("部门成员删除")
    @Override
    public DeptMemberResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody DelDeptMemberRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();

        deptMemberDao.deleteByMemberSeqAndDeptSeq(request.getMemberSeq(), request.getDeptSeq());

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
