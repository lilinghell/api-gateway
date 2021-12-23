package com.hell.dto.request.tag;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QryApiByTagRequest extends BaseRequest {
    @NotNull
    private Integer serviceTagSeq;
    @NotNull
    private Integer appSeq;
}
