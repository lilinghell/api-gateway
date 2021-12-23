package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelApiGroupRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    @NotNull
    private Integer appEnvSeq;
}
