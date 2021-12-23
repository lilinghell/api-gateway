package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

@Data
public class DelEnvRequest extends BaseRequest {
    private Integer seq;
}
