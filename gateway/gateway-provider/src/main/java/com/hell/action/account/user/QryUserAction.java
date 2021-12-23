package com.hell.action.account.user;

import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SUserDao;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.QryUserRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.Response;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "user api")
public class QryUserAction implements BaseAction {

    private SUserDao userDao;

    @GetMapping(path = "/api/t1/account/qryUser")
    @ApiOperation("用户查询")
    public Response execute(HttpServletRequest request, @Valid QryUserRequest qry) throws Exception {
        //当前登陆用户
        SUser user = (SUser) ServiceUtils.getCurrentUser(request);
        List<SUser> tmUserList = new ArrayList<>();
        if (StringUtils.isNotEmpty(qry.getEmail())) {
            SUser u = userDao.findByEmailAndUserTypeAndEntInfoSeqAndSeqNot(qry.getEmail(),
                    Dictionary.USER_TYPE_2,
                    user.getEntInfo().getSeq(),
                    user.getSeq());
            if (null != u) {
                tmUserList.add(u);
            }
        }
        else {
            tmUserList = userDao.findByUserTypeAndEntInfoSeqAndSeqNot(Dictionary.USER_TYPE_2,
                    user.getEntInfo().getSeq(),
                    user.getSeq(),
                    Sort.by(Sort.Direction.DESC, "seq"));
        }
        List<SUser> rList = tmUserList.stream().map(u -> {
            u.setPassword("");
            return u;
        }).collect(Collectors.toList());

        return new ResponseResult(rList);
    }

    @Autowired
    public void setUserDao(SUserDao userDao) {
        this.userDao = userDao;
    }
}
