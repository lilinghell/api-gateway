package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateTestGroupRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    @NotBlank
    private String name;
    private String info;
    @NotNull
    private Integer appSeq;
}
