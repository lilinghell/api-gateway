package com.hell.dto.response.tag;

import com.hell.db.table.provider.SApiServiceTag;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ApiServiceTagResponse extends BaseResponse {
    private List<SApiServiceTag> apiServiceTags;
}
