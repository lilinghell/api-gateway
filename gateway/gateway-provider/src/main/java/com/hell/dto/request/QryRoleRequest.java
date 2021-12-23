package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QryRoleRequest extends BaseRequest {
    @ApiModelProperty("角色名称")
    private String roleName;
}
