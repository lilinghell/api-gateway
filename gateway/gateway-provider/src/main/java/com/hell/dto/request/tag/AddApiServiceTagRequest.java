package com.hell.dto.request.tag;

import com.hell.config.request.BaseRequest;
import lombok.Data;

@Data
public class AddApiServiceTagRequest extends BaseRequest {
    private Integer apiSeq;
    private Integer serviceTagSeq;
    private Integer appSeq;
    private Boolean ticked;
}
