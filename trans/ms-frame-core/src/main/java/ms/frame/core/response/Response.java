package ms.frame.core.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Response<Head> implements Serializable {

    @ApiModelProperty(name = "应答报文头", required = true)
    @NotNull
    private Head head;
}
