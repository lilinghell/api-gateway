package com.hell.action.account.role;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SRoleDao;
import com.hell.db.table.provider.SRole;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.CreateRoleRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "role api")
public class CreateRoleAction implements BaseAction<CreateRoleRequest, ResponseResult> {

    private SRoleDao roleDao;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/t1/account/createRole")
    @ApiOperation("角色创建")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody CreateRoleRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<SRole> t2 = roleDao.findByRoleNameAndEntSeq(request.getRoleName(), entSeq);
        if (ObjectUtils.isNotEmpty(t2)) {
            throw new ValidationException(CheckMsg.VALIDATION_ROLE_EXIST);
        }
        SRole tmRole = new SRole();
        tmRole.setRoleName(request.getRoleName());
        tmRole.setEntSeq(entSeq);
        SRole t = roleDao.save(tmRole);
        return new ResponseResult(t);
    }

    @Autowired
    public void setRoleDao(SRoleDao roleDao) {
        this.roleDao = roleDao;
    }
}
