package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddTestCasesRequest extends BaseRequest {
    @NotBlank
    private String name;
    private String status;
    @NotBlank
    private String apiPath;
    @NotBlank
    private String parameter;
    @NotBlank
    private String assertScript;
    @NotNull
    private Integer testGroupSeq;
    private String caseDesc;
    @NotBlank
    private String caseType;
    @NotNull
    private Integer appSeq;
}
