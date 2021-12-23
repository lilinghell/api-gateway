package com.hell.action.account.role;

import com.hell.common.CheckMsg;
import com.hell.dao.SRoleDao;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DeleteRoleRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "role api")
@RestController
public class DeleteRoleAction implements BaseAction<DeleteRoleRequest, ResponseResult> {

    private SRoleDao roleDao;
    private SUserDao userDao;

    @DeleteMapping(path = "/api/t1/account/deleteRole/{seq}")
    @ApiOperation("角色删除")
    @Override
    public ResponseResult execute(@Valid DeleteRoleRequest request) throws Exception {
        //判断该角色下是否存在用户
        List<SUser> users = userDao.findByRoleInfoSeq(request.getSeq());
        if (ObjectUtils.isNotEmpty(users)) {
            throw new ValidationException(CheckMsg.VALIDATION_DO_NOT_DEL);
        }
        roleDao.deleteById(request.getSeq());
        return new ResponseResult("success");
    }

    @Autowired
    public void setRoleDao(SRoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
