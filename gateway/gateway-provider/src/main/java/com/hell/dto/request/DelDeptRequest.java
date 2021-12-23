package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelDeptRequest extends BaseRequest {
    @NotNull
    private Integer seq;
}
