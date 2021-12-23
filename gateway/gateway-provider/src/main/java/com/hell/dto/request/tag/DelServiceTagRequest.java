package com.hell.dto.request.tag;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelServiceTagRequest {
    @NotNull
    private Integer seq;
    private String color;
    private String name;
}
