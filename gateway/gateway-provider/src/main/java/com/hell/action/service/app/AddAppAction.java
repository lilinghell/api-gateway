package com.hell.action.service.app;

import com.hell.action.service.env.AddAppEnvAction;
import com.hell.action.test.parameter.AddParameterAction;
import com.hell.common.CheckMsg;
import com.hell.common.Dictionary;
import com.hell.common.ServiceUtils;
import com.hell.dao.SAppDao;
import com.hell.db.table.provider.SApp;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.AddParameterRequest;
import com.hell.dto.request.app.AddAppEnvRequest;
import com.hell.dto.request.app.AddAppRequest;
import com.hell.dto.response.app.AppResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "app")
public class AddAppAction implements BaseAction<AddAppRequest, AppResponse> {

    private SAppDao appDao;
    private AddAppEnvAction addAppEnvAction;
    private AddParameterAction addParameterAction;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/t1/service/addApp")
    @ApiOperation("环境添加")
    @Override
    public AppResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddAppRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApp app = appDao.findByNameAndEntSeq(request.getName(), entSeq);
        if (ObjectUtils.isNotEmpty(app)) {
            throw new ValidationException(CheckMsg.VALIDATION_NAME_EXIST);
        }
        app = new SApp();
        BeanUtils.copyProperties(request, app);
        app.setEntSeq(entSeq);

        app = appDao.save(app);

        AppResponse r = new AppResponse();
        List<SApp> l = new ArrayList<>();
        l.add(app);

        r.setApps(l);

        //创建环境
        //createAppEnv(httpServletRequest, app);
        //创建变量
        createParameter(httpServletRequest, app);
        return r;
    }

    private void createParameter(HttpServletRequest httpServletRequest, SApp app) throws Exception {
        AddParameterRequest addParameterRequest = new AddParameterRequest();
        addParameterRequest.setAppSeq(app.getSeq());
        addParameterRequest.setName("X-Request-EnvKey");
        addParameterRequest.setEnName("X-Request-EnvKey");
        addParameterRequest.setType(Dictionary.PARAMETER_TYPE_0);;
        addParameterRequest.setValue("");
        addParameterAction.execute(httpServletRequest, addParameterRequest);
    }

    private void createAppEnv(HttpServletRequest httpServletRequest, SApp app) throws Exception {
        AddAppEnvRequest addAppEnvRequest = new AddAppEnvRequest();
        addAppEnvRequest.setAppSeq(app.getSeq());
        addAppEnvRequest.setName("开发环境");
        addAppEnvRequest.setType("DEV");
        addAppEnvAction.execute(httpServletRequest, addAppEnvRequest);
        addAppEnvRequest.setName("测试环境");
        addAppEnvRequest.setType("TEST");
        addAppEnvAction.execute(httpServletRequest, addAppEnvRequest);
        addAppEnvRequest.setName("验证环境");
        addAppEnvRequest.setType("UAT");
        addAppEnvAction.execute(httpServletRequest, addAppEnvRequest);
    }

    @Autowired
    public void setAppDao(SAppDao appDao) {
        this.appDao = appDao;
    }

    @Autowired
    public void setAddAppEnvAction(AddAppEnvAction addAppEnvAction) {
        this.addAppEnvAction = addAppEnvAction;
    }

    @Autowired
    public void setAddParameterAction(AddParameterAction addParameterAction) {
        this.addParameterAction = addParameterAction;
    }
}
