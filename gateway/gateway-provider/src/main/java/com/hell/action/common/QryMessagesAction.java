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
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@Api(tags = "common")
public class QryMessagesAction implements BaseAction<MessageRequest, ResponseResult> {

    private SMessageDao messageDao;

    @ApiOperation("消息列表")
    @GetMapping(path = "/api/t1/common/qryMessages")
    @Override
    public ResponseResult execute(HttpServletRequest httpServletRequest, MessageRequest request) throws Exception {
        Arrays.stream(httpServletRequest.getCookies()).forEach(cookie -> {
            System.out.println(cookie.getName());
        });
        SUser u = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = u.getEntInfo().getSeq();
        List<SMessage> list = messageDao.findByEntSeq(entSeq,
                Sort.by(Sort.Direction.DESC, "seq"));
        return new ResponseResult(list);
    }

    @Autowired
    public void setMessageDao(SMessageDao messageDao) {
        this.messageDao = messageDao;
    }
}
