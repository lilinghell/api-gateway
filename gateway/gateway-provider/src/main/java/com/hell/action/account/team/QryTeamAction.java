package com.hell.action.account.team;

import com.hell.common.ServiceUtils;
import com.hell.dao.STeamDao;
import com.hell.db.table.provider.STeam;
import com.hell.db.table.provider.SUser;
import com.hell.dto.request.TeamRequest;
import com.hell.dto.response.TeamsResponse;
import com.hell.config.action.BaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api(tags = "team")
public class QryTeamAction implements BaseAction<TeamRequest, TeamsResponse> {

    private STeamDao teamDao;

    @GetMapping(path = "/api/t1/account/qryTeams")
    @ApiOperation("团队信息查询")
    public TeamsResponse qry(HttpServletRequest httpServletRequest) throws Exception {
        SUser user = (SUser) ServiceUtils.getCurrentUser(httpServletRequest);
        Integer entSeq = user.getEntInfo().getSeq();
        List<STeam> list = teamDao.findByEntSeq(entSeq);
        TeamsResponse t = new TeamsResponse();
        t.setTeams(list);
        return t;
    }

    @Autowired
    public void setTeamDao(STeamDao teamDao) {
        this.teamDao = teamDao;
    }
}
