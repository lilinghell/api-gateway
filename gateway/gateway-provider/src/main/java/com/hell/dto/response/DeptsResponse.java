package com.hell.dto.response;

import com.hell.db.table.provider.SDept;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class DeptsResponse extends BaseResponse {
    private List<SDept> depts;
}
