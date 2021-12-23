package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AppEnvRequest extends BaseRequest {
    @NotNull
    private Integer appSeq;
    private Integer appEnvSeq;
}
