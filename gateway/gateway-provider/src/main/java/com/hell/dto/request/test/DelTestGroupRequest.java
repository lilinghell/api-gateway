package com.hell.dto.request.test;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelTestGroupRequest extends BaseRequest {

    @NotNull
    private Integer seq;
}
