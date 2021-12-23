package com.hell.action.account.dept;

import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptMemberDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DeptMemberRequest;
import com.hell.dto.response.DeptMemberResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "dept api")
public class QryDeptMemberAction implements BaseAction<DeptMemberRequest, DeptMemberResponse> {

    private SDeptMemberDao deptMemberDao;

    @GetMapping(path = "/api/t1/account/qryDeptMember")
    @ApiOperation("团队成员信息查询")
    @Override
    public DeptMemberResponse execute(HttpServletRequest re, DeptMemberRequest request) throws Exception {
        SUser cUser = (SUser) ServiceUtils.getCurrentUser(re);
        Integer entSeq = cUser.getEntInfo().getSeq();

        List<SUser> rList = qryDeptMemberInfo(entSeq, request.getDeptSeq());

        DeptMemberResponse r = new DeptMemberResponse();
        r.setMembers(rList);
        return r;
    }

    @Autowired
    public void setDeptMemberDao(SDeptMemberDao deptMemberDao) {
        this.deptMemberDao = deptMemberDao;
    }

    public List<SUser> qryDeptMemberInfo(Integer entSeq, Integer deptSeq) {
        List<SUser> l = deptMemberDao.qryDeptMemberInfo(entSeq, deptSeq);
        List<SUser> rList = l.stream().map(u -> {
            u.setPassword("");
            return u;
        }).collect(Collectors.toList());

        return rList;
    }
}
