package com.hell.action.service.env;

import com.hell.common.ServiceUtils;
import com.hell.dao.SAppEnvDao;
import com.hell.db.table.provider.SAppEnv;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.app.AddAppEnvRequest;
import com.hell.dto.response.app.AppEnvResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "app")
public class AddAppEnvAction implements BaseAction<AddAppEnvRequest, AppEnvResponse> {
    private SAppEnvDao appEnvDao;

    @PostMapping(path = "/api/t1/service/addAppEnv")
    @ApiOperation("生态环境添加")
    @Override
    public AppEnvResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddAppEnvRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        /*SAppEnv result = appEnvDao.findByNameAndEntSeq(request.getName(), entSeq);
        if ( null != result){
            throw new ValidationException(CheckMsg.VALIDATION_APPENV_NOT_SAME);
        }*/
        SAppEnv appEnv = new SAppEnv();
        BeanUtils.copyProperties(request, appEnv);
        appEnv.setEntSeq(entSeq);

        appEnv = appEnvDao.save(appEnv);
        String str = appEnv.getEntSeq() + appEnv.getAppSeq() + appEnv.getSeq() + "_env";
        String envKey = DigestUtils.md5DigestAsHex(str.getBytes());
        appEnv.setEnvKey(envKey);
        appEnvDao.save(appEnv);

        AppEnvResponse r = new AppEnvResponse();
        List<SAppEnv> list = new ArrayList<>();
        list.add(appEnv);

        r.setAppEnvs(list);

        return r;
    }

    @Autowired
    public void setAppEnvDao(SAppEnvDao appEnvDao) {
        this.appEnvDao = appEnvDao;
    }
}
