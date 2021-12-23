package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApiExtRequest extends BaseRequest {
    private Integer apiSeq;
    private Integer groupSeq;
    @NotNull
    private Integer appEnvSeq;
}
