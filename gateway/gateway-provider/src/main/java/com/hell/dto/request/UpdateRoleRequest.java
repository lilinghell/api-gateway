package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateRoleRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    @Pattern( regexp = "^.{1,20}$" , message = "长度为1~20")
    @ApiModelProperty("角色名称")
    @NotNull
    private String roleName;
    @ApiModelProperty("路由信息")
    private String routers;
}
