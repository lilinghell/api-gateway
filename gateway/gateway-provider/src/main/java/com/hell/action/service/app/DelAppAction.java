package com.hell.action.service.app;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SAppDao;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SAppEnv;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.DeleteAppRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "app")
@RestController
public class DelAppAction implements BaseAction<DeleteAppRequest, ResponseResult> {
    private SAppDao sAppDao;
    private SAppEnvDao appEnvDao;

    @DeleteMapping(path = "/api/t1/service/delApp/{seq}")
    @ApiOperation("app删除")
    @Override
    public ResponseResult execute(HttpServletRequest re, @Valid DeleteAppRequest request) throws Exception {
        SUser cUser = (SUser) ServiceUtils.getCurrentUser(re);
        Integer entSeq = cUser.getEntInfo().getSeq();
        List<SAppEnv> sAppEnvs = appEnvDao.findByAppSeqAndEntSeq(request.getSeq(), entSeq, Sort.by(Sort.Direction.DESC, "seq"));
        if (ObjectUtils.isNotEmpty(sAppEnvs)) {
            throw new ValidationException(CheckMsg.VALIDATION_DO_NOT_DEL);
        }
        sAppDao.deleteById(request.getSeq());
        return new ResponseResult("success");
    }

    @Autowired
    public void setsAppDao(SAppDao sAppDao) {
        this.sAppDao = sAppDao;
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
