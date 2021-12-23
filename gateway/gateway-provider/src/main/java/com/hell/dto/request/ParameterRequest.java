package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ParameterRequest extends BaseRequest {
    private Integer seq;
    private String name;
    private String enName;
    private String value;
    @NotNull
    private Integer appSeq;
}
