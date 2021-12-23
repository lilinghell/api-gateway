package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.Email;

@Data
public class QryUserRequest extends BaseRequest {
    @Email
    private String email;
}
