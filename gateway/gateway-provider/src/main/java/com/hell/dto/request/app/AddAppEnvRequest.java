package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class AddAppEnvRequest extends BaseRequest {
    @NotNull
    private Integer appSeq;
    @NotBlank
    @Length(min = 1 , max = 20)
    private String name;
    @NotBlank
    private String type;
    @NotBlank
    private String serviceType;
    private String address;
    private String info;
}
