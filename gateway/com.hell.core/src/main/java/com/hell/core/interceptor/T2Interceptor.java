package com.hell.core.interceptor;

import com.hell.core.command.Command;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

@ConfigurationProperties(
        prefix = "app.t2"
)
@Component
public class T2Interceptor implements BaseHandlerInterceptor, ApplicationContextAware {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private ApplicationContext applicationContext;

    @Getter
    @Setter
    @Value("${app.t2.commands.pre:}")
    private LinkedHashSet<String> commandsPre = new LinkedHashSet<String>();

    @Getter
    @Setter
    @Value("${app.t2.commands.post:}")
    private LinkedHashSet<String> commandsPost = new LinkedHashSet<String>();

    @Getter
    @Setter
    @Value("${app.t2.commands.after:}")
    private LinkedHashSet<String> commandsAfter = new LinkedHashSet<String>();

    @Getter
    @Setter
    @Value("${app.t2.path-patterns:/api/t2/**}")
    private List<String> pathPatterns = new ArrayList<>();

    @Getter
    @Setter
    @Value("${app.t2.exclude-path-patterns:}")
    private List<String> excludePathPatterns = new ArrayList<String>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle api: " + request.getServletPath() + ",startTime: " + System.currentTimeMillis());
        commandExecute(commandsPre, request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        commandExecute(commandsPost, request);
        log.info("afterCompletion api: " + request.getServletPath() + ",endTime: " + System.currentTimeMillis());
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        commandExecute(commandsAfter, request);
        log.info("afterCompletion api: " + request.getServletPath() + ",endTime: " + System.currentTimeMillis());
    }

    public void commandExecute(LinkedHashSet<String> commandSet, HttpServletRequest request) throws Exception {
        for (String commandName : commandSet) {
            if ("".equals(commandName) || "null".equals(commandName)) {
                continue;
            }
            Command command = (Command) applicationContext.getBean(commandName);
            command.execute(request, request.getParameterMap());
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
