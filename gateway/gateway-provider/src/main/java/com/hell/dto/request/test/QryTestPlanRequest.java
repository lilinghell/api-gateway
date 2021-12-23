package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QryTestPlanRequest extends BaseRequest {
    @NotNull
    private Integer appSeq;
    private Integer testPlanSeq;
}
