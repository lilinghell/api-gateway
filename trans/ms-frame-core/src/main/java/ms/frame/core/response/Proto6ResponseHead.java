package ms.frame.core.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Proto6ResponseHead extends ResponseHead {

    @ApiModelProperty(name = "返回码", required = true)
    @NotBlank
    @NotNull
    @Max(6)
    private String rtnCode;

    @ApiModelProperty(name = "返回信息", required = true)
    @NotBlank
    @NotNull
    private String rtnMsg;
}