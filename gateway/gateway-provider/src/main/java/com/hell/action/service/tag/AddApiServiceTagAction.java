package com.hell.action.service.tag;

import com.hell.common.ServiceUtils;
import com.hell.dao.SApiServiceTagDao;
import com.hell.db.table.provider.SApiServiceTag;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.tag.AddApiServiceTagRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "app")
public class AddApiServiceTagAction implements BaseAction<AddApiServiceTagRequest, ResponseResult> {

    private SApiServiceTagDao apiServiceTagDao;

    @Transactional(rollbackFor = Exception.class)
    @PostMapping(path = "/api/t1/service/addApiServiceTag")
    @ApiOperation("tag添加")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddApiServiceTagRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        SApiServiceTag tag = new SApiServiceTag();
        if (request.getTicked()) {
            tag.setAppSeq(request.getAppSeq());
            tag.setServiceTagSeq(request.getServiceTagSeq());
            tag.setApiSeq(request.getApiSeq());
            tag.setEntSeq(entSeq);
            apiServiceTagDao.save(tag);
        } else {
            apiServiceTagDao.deleteByApiSeqAndServiceTagSeqAndEntSeq(request.getApiSeq(), request.getServiceTagSeq(), entSeq);
        }
        return new ResponseResult("success");
    }

    @Autowired
    public void setApiServiceTagDao(SApiServiceTagDao apiServiceTagDao) {
        this.apiServiceTagDao = apiServiceTagDao;
    }
}
