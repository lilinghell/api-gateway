package com.hell.action.account.team;

import com.hell.common.CheckMsg;
import com.hell.common.ServiceUtils;
import com.hell.dao.STeamDao;
import com.hell.db.table.provider.STeam;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.TeamRequest;
import com.hell.dto.response.TeamResponse;
import com.hell.config.action.BaseAction;
import com.hell.core.exception.ValidationException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "team")
public class DelTeamAction implements BaseAction<TeamRequest, TeamResponse> {

    private STeamDao teamDao;

    @DeleteMapping(path = "/api/t1/account/delTeam/{seq}")
    @ApiOperation("团队删除")
    public TeamResponse del(HttpServletRequest httpServletRequest, TeamRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();
        STeam te = teamDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        if (ObjectUtils.isEmpty(te)) {
            throw new ValidationException(CheckMsg.VALIDATION_DO_NOT_DEL);
        }

        teamDao.delete(te);
        return new TeamResponse();
    }

    @Autowired
    public void setTeamDao(STeamDao teamDao) {
        this.teamDao = teamDao;
    }
}
