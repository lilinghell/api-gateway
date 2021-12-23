package com.hell.action.service.tag;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.SServiceTagDao;
import com.hell.db.table.provider.SServiceTag;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.tag.UpdateServiceTagRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "app")
public class UpdateServiceTagAction implements BaseAction<UpdateServiceTagRequest, ResponseResult> {
    private SServiceTagDao sServiceTagDao;

    @PutMapping(path = "/api/t1/service/updateServiceTag")
    @ApiOperation("tag更新")
    @Override
    public ResponseResult execute(HttpServletRequest r, @Valid @RequestBody UpdateServiceTagRequest request) throws Exception{
        SUser u = (SUser) ServiceUtils.getCurrentUser(r);
        Integer entSeq = u.getEntInfo().getSeq();
        SServiceTag result = sServiceTagDao.findByNameAndEntSeq(request.getName(),entSeq);
        if (result != null && (request.getSeq()).compareTo(result.getSeq()) != 0){
            throw new ValidationException(CheckMsg.VALIDATION_SERVICETAG_NAME_NOT_SAME);
        }
        result = sServiceTagDao.findBySeq(request.getSeq());
        result.setName(request.getName());
        SServiceTag save = sServiceTagDao.save(result);
        return new ResponseResult(save);
    }
    @Autowired
    public void setsServiceTagDao(SServiceTagDao sServiceTagDao) {
        this.sServiceTagDao = sServiceTagDao;
    }
}
