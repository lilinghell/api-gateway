package com.hell.dto.request.app;

import com.hell.config.request.BaseRequest;
import lombok.Data;

@Data
public class QryAppRequest extends BaseRequest {
    private Integer appSeq;
}
