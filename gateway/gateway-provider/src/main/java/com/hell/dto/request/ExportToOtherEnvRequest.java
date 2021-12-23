package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ExportToOtherEnvRequest extends BaseRequest {
    @NotNull
    private List<Integer> apiSeqList;
    @NotNull
    private Integer otherEnvSeq;
    private Integer envSeq;
    private Integer appSeq;
}
