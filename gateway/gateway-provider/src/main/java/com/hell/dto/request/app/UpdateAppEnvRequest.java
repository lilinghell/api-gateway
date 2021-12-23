package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
public class UpdateAppEnvRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    @NotNull
    private Integer appSeq;
    @Length(min = 1, max = 20, message = "名称长度不超过20")
    private String name;
    private String address;
    private String info;
}
