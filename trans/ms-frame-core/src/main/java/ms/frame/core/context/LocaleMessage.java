package ms.frame.core.context;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Component
public class LocaleMessage implements Serializable {
    @Resource
    private MessageSource messageSource;

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String getMessage(String code) {
        return this.getMessage(code, new Object[]{});
    }

    public String getMessage(String code, String defaultMessage) {
        return this.getMessage(code, null, defaultMessage);
    }

    public String getMessage(String code, String defaultMessage, Locale locale) {
        return this.getMessage(code, null, defaultMessage, locale);
    }

    public String getMessage(String code, Locale locale) {
        return this.getMessage(code, null, "", locale);
    }

    public String getMessage(String code, Object[] args) {
        return this.getMessage(code, args, "");
    }

    public String getMessage(String code, Object[] args, Locale locale) {
        return this.getMessage(code, args, "", locale);
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        Locale locale = LocaleContextHolder.getLocale();
        return this.getMessage(code, args, defaultMessage, locale);
    }

    public String getMessage(String code, Object[] args, String defaultMessage, Locale locale) {
        String msg = this.messageSource.getMessage(code, args, defaultMessage, locale);
        return StringUtils.isBlank(msg) ?
                StringUtils.toEncodedString(code.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8) :
                StringUtils.toEncodedString(msg.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }
}
