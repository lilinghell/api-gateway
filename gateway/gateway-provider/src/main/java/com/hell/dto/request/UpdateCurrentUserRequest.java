package com.hell.dto.request;

import com.hell.db.table.common.PAttachment;
import com.hell.db.table.provider.SRole;
import com.hell.annotation.validation.UserId;
import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdateCurrentUserRequest extends BaseRequest {
    @NotNull
    private Integer seq;
    @Length(max = 20 , min = 0 ,message = "长度最大不超20")
    private String userName;
    @UserId
    @Length(max = 20 , min = 1 ,message = "长度最大不超20")
    private String userId;
    @Email
    private String email;
    private String password;
    @NotBlank(message = "手机号码不能为空")
    @Pattern( regexp = "[0-9-()（）]{7,11}" ,message = "手机号码格式不正确")
    private String mobilePhone;
    @NotNull
    private SRole roleInfo;
    private PAttachment headIcon;
}
