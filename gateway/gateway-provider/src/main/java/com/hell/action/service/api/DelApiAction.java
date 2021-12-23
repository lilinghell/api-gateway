package com.hell.action.service.api;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiDao;
import com.hell.dao.SApiExtDao;
import com.hell.dao.SApiHisDao;
import com.hell.db.table.provider.SApi;
import com.hell.db.table.provider.SApiExt;
import com.hell.db.table.provider.SApiHis;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.DelApiRequest;
import com.hell.service.NotifyGatewayService;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "api")
public class DelApiAction implements BaseAction<DelApiRequest, ResponseResult> {

    private SApiDao apiDao;
    private SApiExtDao apiExtDao;
    private SApiHisDao apiHisDao;
    private NotifyGatewayService notifyGatewayService;

    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping(path = "/api/t1/service/delApi/{seq}")
    @ApiOperation("API删除")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid DelApiRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();

        SApi api = apiDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        //保存到历史表
        SApiHis apiHis = new SApiHis();
        BeanUtils.copyProperties(api, apiHis);
        apiHisDao.save(apiHis);


        apiDao.deleteBySeqAndEntSeq(request.getSeq(), entSeq);
        SApiExt apiExt = apiExtDao.findByApiSeqAndEntSeq(request.getSeq(), entSeq);
        if (null != apiExt) {
            apiExtDao.deleteById(request.getSeq());
        }
        if(StringUtils.isNotEmpty(api.getUrl())) {
            notifyGatewayService.notifyGateway(request.getSeq(), api.getUrl(), api.getAppEnvSeq());
        }

        return new ResponseResult("success");
    }

    @Autowired
    public void setApiDao(SApiDao apiDao) {
        this.apiDao = apiDao;
    }

    @Autowired
    public void setApiExtDao(SApiExtDao apiExtDao) {
        this.apiExtDao = apiExtDao;
    }

    @Autowired
    public void setNotifyGatewayService(NotifyGatewayService notifyGatewayService) {
        this.notifyGatewayService = notifyGatewayService;
    }

    @Autowired
    public void setApiHisDao(SApiHisDao apiHisDao) {
        this.apiHisDao = apiHisDao;
    }
}
