package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class TestRunRequest extends BaseRequest {

    @NotNull
    private List<Integer> caseSeqList;
    @NotNull
    private Integer appSeq;
}
