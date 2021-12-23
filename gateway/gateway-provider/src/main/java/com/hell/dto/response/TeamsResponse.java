package com.hell.dto.response;

import com.hell.db.table.provider.STeam;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TeamsResponse extends BaseResponse {
    private List<STeam> teams;
}
