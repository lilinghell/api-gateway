package com.hell.dto.response;

import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class TokenResponse extends BaseResponse {
    private String _tokenName;
}
