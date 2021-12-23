package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class AddAppRequest extends BaseRequest {
    @NotBlank
    @Length(max = 20 , min = 1 ,message = "长度最大不超20")
    private String name;
    @NotBlank
    private String type;
    @Length(max = 256 , min = 0 ,message = "长度最大不超256")
    private String info;
    private String serviceId;
}
