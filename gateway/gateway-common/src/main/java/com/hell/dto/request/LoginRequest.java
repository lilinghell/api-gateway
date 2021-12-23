package com.hell.dto.request;


import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class LoginRequest extends BaseRequest {

    @NotBlank
    @ApiModelProperty("登陆Id")
    private String loginId;
    @NotBlank
    @ApiModelProperty("登陆密码")
    private String loginPassword;
}
