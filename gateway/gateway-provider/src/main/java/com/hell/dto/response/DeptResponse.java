package com.hell.dto.response;

import com.hell.db.table.provider.SDept;
import com.hell.config.response.BaseResponse;
import lombok.Data;

@Data
public class DeptResponse extends BaseResponse {
    private SDept dept;
}
