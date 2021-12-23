package com.hell.dto.request;

import com.hell.db.table.common.PAttachment;
import com.hell.db.table.provider.SRole;
import com.hell.annotation.validation.UserId;
import com.hell.config.request.BaseRequest;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CreateUserRequest extends BaseRequest {
    @UserId
    @Length(max = 20 , min = 1 ,message = "长度最大不超20")
    private String userId;
    private String userName;
    @NotNull
    private SRole role;
    @Pattern( regexp = "^[a-z0-9_-]{6,18}$" ,message = "6—18位")
    private String password;
    private String email;
    @Pattern( regexp = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$" ,message = "手机号码格式不正确")
    private String mobilePhone;
    private PAttachment headIcon;
}
