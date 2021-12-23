package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdatePassRequest extends BaseRequest {
    @NotNull
    @Pattern( regexp = "^[a-z0-9_-]{6,18}$" ,message = "6—18位")
    private String oldPassword;
    @NotNull
    @Pattern( regexp = "^[a-z0-9_-]{6,18}$" ,message = "6—18位")
    private  String newPassword;
    @NotNull
    @Pattern( regexp = "^[a-z0-9_-]{6,18}$" ,message = "6—18位")
    private String confirmPassword;
}
