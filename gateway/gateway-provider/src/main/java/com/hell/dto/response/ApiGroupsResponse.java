package com.hell.dto.response;

import com.hell.db.table.provider.SApiGroup;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApiGroupsResponse extends BaseResponse {
    private List<SApiGroup> apiGroups;
}
