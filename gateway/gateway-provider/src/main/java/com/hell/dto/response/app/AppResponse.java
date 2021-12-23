package com.hell.dto.response.app;

import com.hell.db.table.provider.SApp;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class AppResponse extends BaseResponse {
    private List<SApp> apps;
}
