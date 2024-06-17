package ms.frame.core.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Proto3ResponseHead extends ResponseHead {
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

    @ApiModelProperty("提供方系统ID")
    @Max(32)
    @NotBlank
    @NotNull
    private String prodSysId;

    @ApiModelProperty("提供方代码")
    @Max(32)
    @NotBlank
    @NotNull
    private String prodSysCode;

    @ApiModelProperty("提供方流水号")
    @Max(32)
    @NotBlank
    @NotNull
    private String prodSrlNo;

    @ApiModelProperty("返回码")
    @Max(32)
    @NotBlank
    @NotNull
    private String rtnCode;

    @ApiModelProperty("返回信息")
    @Max(32)
    @NotBlank
    @NotNull
    private String rtnMsg;

    @ApiModelProperty("客户号")
    @Max(32)
    private String cifNo;

    @ApiModelProperty("用户编号")
    @Max(32)
    private String usrNo;
}
