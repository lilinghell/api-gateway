package com.hell.dto.request;

import com.hell.config.request.BaseRequest;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ApiExportRequest extends BaseRequest {
    @ApiModelProperty("超时时间是否导出")
    private Boolean timeOutFlg;
    @ApiModelProperty("限流是否导出")
    private Boolean rateFlg;
    @ApiModelProperty("Api Mock数据是否导出")
    private Boolean apiMockFlg;
    @ApiModelProperty("导出的接口seq列表")
    private List<Integer> selectApis;
    @NotNull
    private Integer appSeq;
}
