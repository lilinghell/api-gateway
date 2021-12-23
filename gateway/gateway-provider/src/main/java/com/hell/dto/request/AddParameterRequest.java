package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddParameterRequest extends BaseRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String enName;
    @NotNull
    private Integer appSeq;
    @NotBlank
    private String value;
    @NotBlank
    private String type;
}
