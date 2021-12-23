package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class SendSmsRequest extends BaseRequest {

    @ApiModelProperty("手机号")
    @Length(max = 11, min = 11)
    @NotBlank
    private String mobilePhone;
    @ApiModelProperty("国际区号")
    @Length(max = 5)
    private String globalRoaming;
}
