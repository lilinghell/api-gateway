package com.hell.dto.response;

import com.hell.db.table.provider.SApi;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApisResponse extends BaseResponse {
    private List<SApi> apis;
}
