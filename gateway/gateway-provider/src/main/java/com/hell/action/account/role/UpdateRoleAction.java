package com.hell.action.account.role;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SRoleDao;
import com.hell.db.table.provider.SRole;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.UpdateRoleRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "role api")
@RestController
public class UpdateRoleAction implements BaseAction<UpdateRoleRequest, ResponseResult> {

    private SRoleDao roleDao;

    @PutMapping(path = "/api/t1/account/updateRole")
    @ApiOperation("角色更新")
    @Override
    public ResponseResult execute(HttpServletRequest re, @Valid @RequestBody UpdateRoleRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(re);
        Integer entSeq = u.getEntInfo().getSeq();

        //判断角色名称是否存在
        List<SRole> list = roleDao.findByRoleNameAndEntSeqAndSeqNot(request.getRoleName(), entSeq, request.getSeq());
        if (ObjectUtils.isNotEmpty(list)) {
            throw new ValidationException(CheckMsg.VALIDATION_ROLE_EXIST);
        }
        SRole tmRole = roleDao.findBySeq(request.getSeq());
        tmRole.setRoleName(request.getRoleName());
        tmRole.setRouters(request.getRouters());
        roleDao.save(tmRole);
        return new ResponseResult(tmRole);
    }

    @Autowired
    public void setRoleDao(SRoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
