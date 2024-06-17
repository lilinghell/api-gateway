package ms.frame.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class Proto3RequestVo extends Request {
    @ApiModelProperty(name = "请求报文头", required = true)
    @NotNull
    private Proto3RequestHead head;
}
