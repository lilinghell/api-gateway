package com.hell.dto.response;

import com.hell.db.table.provider.STeam;
import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class TeamResponse extends BaseResponse {
    private STeam team;
}
