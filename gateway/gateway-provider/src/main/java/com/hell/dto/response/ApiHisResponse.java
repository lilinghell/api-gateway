package com.hell.dto.response;

import com.hell.db.table.provider.SApiHis;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApiHisResponse extends BaseResponse {
    private List<SApiHis> apis;
}
