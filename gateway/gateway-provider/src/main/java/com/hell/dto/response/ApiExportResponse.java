package com.hell.dto.response;

import com.hell.entity.api.ApiInfo;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApiExportResponse extends BaseResponse {
    private List<ApiInfo> apis;
}
