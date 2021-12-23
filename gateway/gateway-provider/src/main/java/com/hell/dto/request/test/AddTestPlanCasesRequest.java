package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddTestPlanCasesRequest extends BaseRequest {
    @NotNull
    private Integer appSeq;
    @NotNull
    private Integer testPlanSeq;
    @NotNull
    private List<Integer> caseSeqList;
}
