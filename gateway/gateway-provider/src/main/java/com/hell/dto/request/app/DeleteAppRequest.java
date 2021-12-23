package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteAppRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    private String name;
    private String type;
    private String info;
}
