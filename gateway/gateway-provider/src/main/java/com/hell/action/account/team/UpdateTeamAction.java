package com.hell.action.account.team;

import com.hell.common.ServiceUtils;
import com.hell.dao.STeamDao;
import com.hell.db.table.provider.STeam;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.TeamRequest;
import com.hell.dto.response.TeamResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@Api(tags = "team")
public class UpdateTeamAction implements BaseAction<TeamRequest, TeamResponse> {

    private STeamDao teamDao;

    @PostMapping(path = "/api/t1/account/updateTeam")
    @ApiOperation("团队信息更新")
    public TeamResponse add(HttpServletRequest httpServletRequest, @Valid TeamRequest request) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();

        STeam team = teamDao.findBySeqAndEntSeq(request.getSeq(), entSeq);
        BeanUtils.copyProperties(request, team);

        team = teamDao.save(team);

        TeamResponse r = new TeamResponse();
        r.setTeam(team);
        return r;
    }

    @Autowired
    public void setTeamDao(STeamDao teamDao) {
        this.teamDao = teamDao;
    }
}
