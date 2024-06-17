package ms.frame.core.application;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Component
@ConfigurationProperties("ms.app")
@Validated
public class ApplicationProperties {
    private final Log logger = LogFactory.getLog(this.getClass());
    @NotBlank
    @Valid
    private String id;

    @NotBlank
    @Valid
    private String code;

    public ApplicationProperties() {
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public String getId() {
        return id;
    }
}
