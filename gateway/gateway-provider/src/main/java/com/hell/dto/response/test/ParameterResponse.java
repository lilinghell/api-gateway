package com.hell.dto.response.test;

import com.hell.db.table.provider.SParameter;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ParameterResponse extends BaseResponse {

    private List<SParameter> parameters;
}
