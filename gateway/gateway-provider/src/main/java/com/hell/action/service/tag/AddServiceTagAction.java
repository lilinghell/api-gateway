package com.hell.action.service.tag;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SServiceTagDao;
import com.hell.db.table.provider.SServiceTag;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.tag.AddServiceTagRequest;
import com.hell.dto.response.tag.ServiceTagResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@Api(tags = "app")
public class AddServiceTagAction implements BaseAction<AddServiceTagRequest, ServiceTagResponse> {

    private SServiceTagDao serviceTagDao;

    @PostMapping(path = "/api/t1/service/addServiceTag")
    @ApiOperation("tag添加")
    @Override
    public ServiceTagResponse execute(HttpServletRequest httpServletRequest, @Valid @RequestBody AddServiceTagRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        SServiceTag result = serviceTagDao.findByNameAndEntSeq(request.getName(),entSeq);
        if (null != result){
            throw new ValidationException(CheckMsg.VALIDATION_TAG_NOT_SAME);
        }
        SServiceTag tag = new SServiceTag();
        tag.setAppSeq(request.getAppSeq());
        tag.setEntSeq(entSeq);
        tag.setName(request.getName());
        tag.setColor(request.getColor());

        tag = serviceTagDao.save(tag);
        List<SServiceTag> list = new ArrayList<>();
        list.add(tag);
        ServiceTagResponse r = new ServiceTagResponse();
        r.setServiceTags(list);
        return r;
    }

    @Autowired
    public void setServiceTagDao(SServiceTagDao serviceTagDao) {
        this.serviceTagDao = serviceTagDao;
    }
}
