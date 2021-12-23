package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddTestGroupRequest extends BaseRequest {

    @NotBlank
    private String name;
    private String info;
    private Integer parentSeq;
    @NotNull
    private Integer appSeq;
}
