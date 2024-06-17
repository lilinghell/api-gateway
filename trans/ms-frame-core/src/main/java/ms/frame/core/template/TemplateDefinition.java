package ms.frame.core.template;

import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@Validated
public class TemplateDefinition {
    @NotBlank
    @Valid
    private String id;

    private String templateClassName;

    private LinkedHashSet<String> commandsPre = new LinkedHashSet<String>();

    private LinkedHashSet<String> commandsPost = new LinkedHashSet<String>();

    private LinkedHashSet<String> commandsAfter = new LinkedHashSet<String>();

    @NotEmpty
    @Valid
    private List<String> pathPatterns = new ArrayList<>();

    private List<String> excludePathPatterns = new ArrayList<String>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedHashSet<String> getCommandsPre() {
        return commandsPre;
    }

    public void setCommandsPre(LinkedHashSet<String> commandsPre) {
        this.commandsPre = commandsPre;
    }

    public LinkedHashSet<String> getCommandsPost() {
        return commandsPost;
    }

    public void setCommandsPost(LinkedHashSet<String> commandsPost) {
        this.commandsPost = commandsPost;
    }

    public LinkedHashSet<String> getCommandsAfter() {
        return commandsAfter;
    }

    public void setCommandsAfter(LinkedHashSet<String> commandsAfter) {
        this.commandsAfter = commandsAfter;
    }

    public List<String> getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(List<String> pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    public List<String> getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(List<String> excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    public void setTemplateClassName(String templateClassName) {
        this.templateClassName = templateClassName;
    }

    public String getTemplateClassName() {
        return templateClassName;
    }
}
