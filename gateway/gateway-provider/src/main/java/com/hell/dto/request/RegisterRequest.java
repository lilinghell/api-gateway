package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest extends BaseRequest {

    @Length(max = 11)
    @NotBlank
    private String mobilePhone;
    @Length(max = 11)
    @NotBlank
    @ApiModelProperty("短信验证码")
    private String code;
    @NotBlank
    private String _tokenName;
    @NotBlank
    private String password;
    @NotBlank
    private String devType;
}
