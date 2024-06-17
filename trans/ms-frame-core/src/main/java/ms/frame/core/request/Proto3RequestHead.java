package ms.frame.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class Proto3RequestHead{
    @ApiModelProperty("api版本")
    @Size(max = 64)
    private String version;

    @ApiModelProperty("全局流水号")
    @Max(32)
    @NotBlank
    @NotNull
    private String glbSrlNo;

    @ApiModelProperty("消费方系统ID")
    @Max(32)
    @NotBlank
    @NotNull
    private String csmSysId;

    @ApiModelProperty("消费方代码")
    @Max(32)
    @NotBlank
    @NotNull
    private String csmSysCode;

    @ApiModelProperty("消费方流水号")
    @Max(32)
    @NotBlank
    @NotNull
    private String csmSrlNo;

    @ApiModelProperty("交易日期")
    @Size(min = 8, max = 8)
    @NotBlank
    @NotNull
    private String trsDate;

    @ApiModelProperty("交易时间")
    @Size(min = 6, max = 6)
    @NotBlank
    @NotNull
    private String trsTime;

    @ApiModelProperty("客户号")
    @Max(32)
    private String cifNo;

    @ApiModelProperty("用户编号")
    @Max(32)
    private String usrNo;
}