package com.hell.action.test.parameter;

import com.hell.common.ServiceUtils;
import com.hell.dao.SParameterDao;
import com.hell.db.table.provider.SParameter;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.ParameterRequest;
import com.hell.dto.response.test.ParameterResponse;
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
@Api(tags = "test")
public class QryParameterAction implements BaseAction<ParameterRequest, ParameterResponse> {
    private SParameterDao parameterDao;

    @GetMapping(path = "/api/t1/test/qryParameter")
    @ApiOperation("参数变量查询")
    @Override
    public ParameterResponse execute(HttpServletRequest httpServletRequest, ParameterRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SParameter> list = parameterDao.findByAppSeqAndEntSeq(request.getAppSeq(), entSeq, Sort.by(Sort.Direction.DESC,"seq"));

        ParameterResponse re = new ParameterResponse();
        re.setParameters(list);

        return re;
    }

    @Autowired
    public void setParameterDao(SParameterDao parameterDao) {
        this.parameterDao = parameterDao;
    }
}
