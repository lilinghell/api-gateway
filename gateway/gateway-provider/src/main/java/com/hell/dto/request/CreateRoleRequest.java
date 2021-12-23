package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreateRoleRequest extends BaseRequest {
    @NotBlank
    @Pattern( regexp = "^.{1,20}$" , message = "长度为1~20")
    @ApiModelProperty("角色名称")
    private String roleName;
}
