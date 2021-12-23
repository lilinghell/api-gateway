package com.hell.dto.response.app;

import com.hell.db.table.provider.SAppEnv;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class AppEnvResponse extends BaseResponse {
    private List<SAppEnv> appEnvs;
}
