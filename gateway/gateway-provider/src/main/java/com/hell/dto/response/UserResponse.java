package com.hell.dto.response;

import com.hell.db.table.provider.SUser;
import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class UserResponse extends BaseResponse {
    private SUser user;
}
