package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QryApiRequest extends BaseRequest {

    private Integer groupSeq;
    @NotNull
    private Integer appEnvSeq;
}
