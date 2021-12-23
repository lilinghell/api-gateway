package com.hell.dto.request;

import com.hell.db.table.provider.SApiGroup;
import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddApiGroupRequest extends BaseRequest {
    @NotBlank
    private String name;
    private SApiGroup parent;
    @NotNull
    private Integer appSeq;
}
