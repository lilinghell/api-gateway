package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TeamRequest extends BaseRequest {
    private Integer seq;
    private Integer parenSeq;
    @NotBlank
    private String teamName;
    private String info;
    @NotBlank
    private String status;
}
