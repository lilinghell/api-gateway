package ms.frame.core.template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties("ms.frame")
@Validated
public class TemplateProperties {
    private final Log logger = LogFactory.getLog(this.getClass());
    @NotNull
    @Valid
    private List<TemplateDefinition> templates = new ArrayList<>();

    public TemplateProperties() {
    }

    public List<TemplateDefinition> getTemplates() {
        return templates;
    }

    public void setTemplates(List<TemplateDefinition> templates) {
        this.templates = templates;
        if (templates != null && templates.size() > 0 && this.logger.isDebugEnabled()) {
            this.logger.debug("template supplied from hell Properties: " + templates);
        }
    }
}
