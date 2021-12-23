package com.hell.dto.response.test;

import com.hell.db.table.provider.STestCases;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TestCasesResponse extends BaseResponse {

    private List<STestCases> cases;
}
