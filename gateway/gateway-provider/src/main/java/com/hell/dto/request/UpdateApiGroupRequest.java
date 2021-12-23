package com.hell.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateApiGroupRequest extends AddApiGroupRequest{
    @NotNull
    private Integer seq;
}
