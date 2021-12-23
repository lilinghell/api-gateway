package com.hell.dto.response.test;

import com.hell.db.table.provider.STestPlan;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class TestPlanResponse extends BaseResponse {
    private List<STestPlan> testPlans;
}
