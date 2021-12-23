package com.hell.dto.response;

import com.hell.db.table.provider.SApiGroup;
import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class ApiGroupResponse extends BaseResponse {
    private SApiGroup apiGroup;
}
