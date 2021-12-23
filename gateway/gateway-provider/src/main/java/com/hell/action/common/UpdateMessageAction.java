package com.hell.action.common;

import com.hell.common.ServiceUtils;
import com.hell.dao.SMessageDao;
import com.hell.db.table.provider.SMessage;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.MessageRequest;
import com.hell.config.action.BaseAction;
import com.hell.config.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "common")
public class UpdateMessageAction implements BaseAction<MessageRequest, ResponseResult> {

    private SMessageDao messageDao;

    @PutMapping(path = "/api/t1/common/updateMessage")
    @ApiOperation("message更新")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, @Valid @RequestBody MessageRequest request) throws Exception {
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        SMessage message = messageDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        message.setStatus(request.getStatus());
        messageDao.save(message);
        return new ResponseResult(message);
    }

    @Autowired
    public void setMessageDao(SMessageDao messageDao) {
        this.messageDao = messageDao;
    }
}
