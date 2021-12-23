package com.hell.action.service.env;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SAppEnv;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.UpdateAppEnvRequest;
import com.hell.dto.response.app.AppEnvResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Api(tags = "app")
public class UpdateAppEnvAction implements BaseAction<UpdateAppEnvRequest, AppEnvResponse> {
    private SAppEnvDao appEnvDao;

    @PutMapping(path = "/api/t1/service/updateAppEnv")
    @ApiOperation("环境更新")
    @Override
    public AppEnvResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody UpdateAppEnvRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        SAppEnv result = appEnvDao.findByNameAndEntSeq(request.getName(), entSeq);
        if (null != result && (request.getSeq()).compareTo(result.getSeq()) != 0) {
            throw new ValidationException(CheckMsg.VALIDATION_APPENV_NOT_SAME);
        }

        List<SAppEnv> env = appEnvDao.findBySeqAndEntSeq(request.getSeq(), entSeq);

        env = env.stream().map(appEnv -> {
            appEnv.setAddress(request.getAddress());
            appEnv.setName(request.getName());
            appEnv.setInfo(request.getInfo());
            appEnv = appEnvDao.save(appEnv);
            return appEnv;
        }).collect(Collectors.toList());

        AppEnvResponse r = new AppEnvResponse();
        r.setAppEnvs(env);
        return r;
    }


    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
