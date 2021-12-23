package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddApiRequest extends BaseRequest {

    private String name;
    @NotNull
    private Integer appEnvSeq;
    @NotBlank
    private String version;
    @NotBlank
    private String schemaType;
    @NotBlank
    private String schemaFormat;
    @NotBlank
    private String url;
    private String method;
    private String description;
    @NotBlank
    private String status;
    @NotNull
    private Integer groupSeq;
    private String info;
    @NotBlank
    private String flowSwitch;
    @NotBlank
    private String mockSwitch;
    private String detail;

    private String serviceId;
}
