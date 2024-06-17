package ms.frame.core.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class Proto6RequestHead{
    @ApiModelProperty("IP")
    @Size(max = 64)
    private String ip;

    @ApiModelProperty("MAC")
    @Size(max = 64)
    private String mac;

    @ApiModelProperty("唯一ID")
    @Size(max = 64)
    private String uuid;

    @ApiModelProperty("操作系统")
    @Size(max = 64)
    private String os;

    @ApiModelProperty("浏览器")
    @Size(max = 128)
    private String browser;

    @ApiModelProperty("验签密文")
    private String signData;

    @ApiModelProperty("验签原文")
    private String originalData;

    @ApiModelProperty("公钥")
    private String pubKey;

    @ApiModelProperty("api版本")
    @Size(max = 64)
    private String version;
}
