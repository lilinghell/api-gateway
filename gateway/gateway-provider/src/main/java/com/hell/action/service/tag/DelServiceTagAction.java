package com.hell.action.service.tag;

import com.hell.common.CheckMsg;
import com.hell.dao.SApiServiceTagDao;
import com.hell.dao.SServiceTagDao;
import com.hell.db.table.provider.SApiServiceTag;
import com.hell.dto.request.tag.DelServiceTagRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
@Api(tags = "app")
public class DelServiceTagAction implements BaseAction<DelServiceTagRequest, ResponseResult> {
    private SServiceTagDao sServiceTagDao;
    private SApiServiceTagDao sApiServiceTagDao;

    @DeleteMapping(path = "/api/t1/service/delServiceTag/{seq}")
    @ApiOperation("tag删除")
    @Override
    public ResponseResult execute( @Valid DelServiceTagRequest request) throws Exception {
        List<SApiServiceTag> sApiServiceTag = sApiServiceTagDao.findByServiceTagSeq(request.getSeq());
        if (ObjectUtils.isNotEmpty(sApiServiceTag)) {
            throw new ValidationException(CheckMsg.VALIDATION_DO_NOT_DEL);
        }
        sServiceTagDao.deleteById(request.getSeq());
        return new ResponseResult("success");
    }
    @Autowired
    public void setsServiceTagDao(SServiceTagDao sServiceTagDao) {
        this.sServiceTagDao = sServiceTagDao;
    }
    @Autowired
    public void setsApiServiceTagDao(SApiServiceTagDao sApiServiceTagDao) {
        this.sApiServiceTagDao = sApiServiceTagDao;
    }
}
