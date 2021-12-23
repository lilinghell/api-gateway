package com.hell.dto.response.test;

import com.hell.db.table.provider.STestGroup;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TestGroupResponse extends BaseResponse {
    private List<STestGroup> testGroups;
}
