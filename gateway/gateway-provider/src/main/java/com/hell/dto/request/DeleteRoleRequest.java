package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteRoleRequest extends BaseRequest {
    @NotNull()
    private Integer seq;
//    @Length()
    private String roleName;
}

