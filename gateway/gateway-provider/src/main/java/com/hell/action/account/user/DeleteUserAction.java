package com.hell.action.account.user;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptMemberDao;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DeleteuserRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;


@RestController
@Api(tags = "user api")
public class DeleteUserAction implements BaseAction<DeleteuserRequest, ResponseResult> {
    private SUserDao userDao;
    private SDeptMemberDao deptMemberDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/account/deleteUser/{seq}")
    @ApiOperation("删除用户")
    @Override
    public ResponseResult execute(HttpServletRequest re, @Valid DeleteuserRequest request) throws Exception {

        SUser cUser = (SUser) ServiceUtils.getCurrentUser(re);
        Integer entSeq = cUser.getEntInfo().getSeq();

        SUser user = userDao.findById(request.getSeq()).get();
        if (entSeq != user.getEntInfo().getSeq()) {
            //防越权，只能删除本企业
            throw new ValidationException(CheckMsg.VALIDATION_UNAUTH_OPERATION);
        }
//        //判断该用户是不是部门成员
//        List<SUser> sd = deptMemberDao.findByMemberSeq(request.getSeq());
//        if (null != sd) {
//            throw new ValidationException(CheckMsg.VALIDATION_DEL_FAIL);
//        }
        userDao.deleteById(request.getSeq());
        deptMemberDao.deleteByMemberSeq(request.getSeq());//删除部门成员表
        return new ResponseResult("success");
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setDeptMemberDao(SDeptMemberDao deptMemberDao) {
        this.deptMemberDao = deptMemberDao;
    }
}
