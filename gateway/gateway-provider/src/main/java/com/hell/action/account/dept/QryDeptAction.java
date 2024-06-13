package com.hell.action.account.dept;

import com.hell.common.ServiceUtils;
import com.hell.dao.SDeptDao;
import com.hell.db.table.provider.SDept;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DeptRequest;
import com.hell.dto.response.DeptsResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = "dept api")
public class QryDeptAction implements BaseAction<DeptRequest, DeptsResponse> {

    private SDeptDao deptDao;

    @GetMapping(path = "/api/t3/account/qryDept")
    @ApiOperation("团队信息查询")
    public DeptsResponse execute(HttpServletRequest httpServletRequest) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();

        List<SDept> list = deptDao.findByEntSeq(entSeq, Sort.by(Sort.Direction.DESC, "seq"));

        DeptsResponse r = new DeptsResponse();
        r.setDepts(list);
        return r;
    }

    @Autowired
    public void setDeptDao(SDeptDao deptDao) {
        this.deptDao = deptDao;
    }
}
