package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateAppRequest extends BaseRequest {
    @NotNull
    private Integer appSeq;
    @Length(max = 256, min = 0, message = "长度最大不超256")
    private String info;
    @NotBlank
    @Length(max = 20, min = 1, message = "长度最大不超20")
    private String name;
    @NotBlank
    private String type;
}
