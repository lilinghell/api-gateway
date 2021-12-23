package com.hell.dto.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateApiRequest extends AddApiRequest {
    @NotNull
    private Integer seq;
    private String clickTabFlag;
}
