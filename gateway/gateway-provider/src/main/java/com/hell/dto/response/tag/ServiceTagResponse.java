package com.hell.dto.response.tag;

import com.hell.db.table.provider.SServiceTag;
import com.hell.config.response.BaseResponse;
import lombok.Data;

import java.util.List;

@Data
public class ServiceTagResponse extends BaseResponse {
    private List<SServiceTag> serviceTags;
}
