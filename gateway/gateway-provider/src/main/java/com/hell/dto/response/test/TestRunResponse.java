package com.hell.dto.response.test;

import com.hell.db.table.provider.STestRun;
import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class TestRunResponse extends BaseResponse {

    private STestRun runResult;
}
