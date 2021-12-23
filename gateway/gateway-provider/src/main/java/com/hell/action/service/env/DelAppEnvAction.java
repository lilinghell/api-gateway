package com.hell.action.service.env;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.DelAppEnvRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@Api(tags = "api")
public class DelAppEnvAction implements BaseAction<DelAppEnvRequest, ResponseResult> {

    private SAppEnvDao appEnvDao;
    private SApiDao apiDao;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/service/delAppEnv/{appEnvSeq}")
    @ApiOperation("API删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelAppEnvRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        List<SApi> l = apiDao.findByEntSeqAndAppEnvSeq(entSeq, request.getAppEnvSeq(), Sort.by(Sort.Direction.DESC, "seq"));
        if (l.size() > 0) {
            throw new ValidationException(CheckMsg.VALIDATION_DO_NOT_DEL);
        }
        appEnvDao.deleteById(request.getAppEnvSeq());

        return new ResponseResult("success");
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }
}
