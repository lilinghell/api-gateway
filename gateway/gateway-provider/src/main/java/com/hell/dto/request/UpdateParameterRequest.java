package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateParameterRequest extends BaseRequest {
    @NotNull
    private Integer Seq;
    @NotBlank
    private String name;
    @NotBlank
    private String enName;
    @NotBlank
    private String value;
    @NotBlank
    private String type;
    @NotNull
    private Integer appSeq;
}
