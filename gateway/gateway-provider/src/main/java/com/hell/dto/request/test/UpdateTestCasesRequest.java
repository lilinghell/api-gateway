package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTestCasesRequest extends BaseRequest {

    @NotNull
    private Integer seq;
    @NotBlank
    private String name;
    @NotBlank
    private String parameter;
    @NotBlank
    private String assertScript;
    private String caseDesc;
    @NotBlank
    private String caseType;
    @NotNull
    private Integer appSeq;
}
