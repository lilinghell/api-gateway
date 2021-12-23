package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DelDeptMemberRequest extends BaseRequest {
    @NotNull
    private Integer memberSeq;
    @NotNull
    private Integer deptSeq;
}
