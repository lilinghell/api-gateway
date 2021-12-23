package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class QryTestCasesRequest extends BaseRequest {
    private Integer testGroupSeq;
    private String apiPath;
    private List<Integer> caseSeqList;
    private Integer caseSeq;
    @NotNull
    private Integer appSeq;
}
