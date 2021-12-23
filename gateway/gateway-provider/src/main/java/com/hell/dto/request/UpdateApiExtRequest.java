package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UpdateApiExtRequest extends BaseRequest {
    @NotNull
    private Integer apiSeq;
    private String connectTimeout;
    private String responseTimeout;
    private String replenishRate;
    private String burstCapacity;
    private String apiMock;
}
