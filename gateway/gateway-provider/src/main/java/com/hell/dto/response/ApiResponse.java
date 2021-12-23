package com.hell.dto.response;

import com.hell.db.table.provider.SApi;
import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class ApiResponse extends BaseResponse {
    private SApi api;
}
