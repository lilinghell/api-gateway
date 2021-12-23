package com.hell.dto.response;

import com.hell.db.table.provider.SUser;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class DeptMemberResponse extends BaseResponse {
    private List<SUser> members;
}
