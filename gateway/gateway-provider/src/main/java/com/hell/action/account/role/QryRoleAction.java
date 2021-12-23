package com.hell.action.account.role;

import com.hell.common.ServiceUtils;
import com.hell.dao.SRoleDao;
import com.hell.db.table.provider.SRole;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.QryRoleRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "role api")
@RestController
public class QryRoleAction implements BaseAction<QryRoleRequest, ResponseResult> {

    private SRoleDao roleDao;

    @GetMapping(path = "/api/t1/account/qryRole")
    @ApiOperation("角色查询")
    @Override
    public ResponseResult execute(HttpServletRequest re, @Valid QryRoleRequest request) throws Exception {
       //只有超级管理员（认证人员才有权限做）
        SUser u = (SUser) ServiceUtils.getCurrentUser(re);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SRole> tmRoleList = null;
        if (StringUtils.isEmpty(request.getRoleName())) {
            tmRoleList = roleDao.findByEntSeq(entSeq, Sort.by(Sort.Direction.DESC, "seq"));
        } else {
            tmRoleList = roleDao.findByRoleNameAndEntSeq(request.getRoleName(), entSeq);
        }
        tmRoleList = tmRoleList.stream()
                .filter(role -> role.getSeq() != u.getRoleInfo().getSeq())
                .collect(Collectors.toList());

        return new ResponseResult(tmRoleList);
    }

    @Autowired
    public void setRoleDao(SRoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
