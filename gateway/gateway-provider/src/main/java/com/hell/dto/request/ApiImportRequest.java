package com.hell.dto.request;

import com.hell.entity.api.ApiExportInfo;
import com.hell.config.request.BaseRequest;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ApiImportRequest extends BaseRequest {
    private ApiExportInfo apiExportInfo;
    private Boolean apiGroupFlg;
    @NotNull
    private Integer appEnvSeq;
}
