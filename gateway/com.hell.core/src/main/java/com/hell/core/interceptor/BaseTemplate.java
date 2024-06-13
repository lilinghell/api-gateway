package com.hell.core.interceptor;

import com.hell.core.command.Command;
import com.hell.core.context.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class BaseTemplate implements BaseHandlerInterceptor {
    private SpringUtils springUtils;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private LinkedHashSet<String> commandsPre = new LinkedHashSet<String>();
    private LinkedHashSet<String> commandsPost = new LinkedHashSet<String>();
    private LinkedHashSet<String> commandsAfter = new LinkedHashSet<String>();
    private List<String> pathPatterns = new ArrayList<>();
    private List<String> excludePathPatterns = new ArrayList<String>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        commandExecute(commandsPre, request);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        commandExecute(commandsPost, request);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        commandExecute(commandsAfter, request);
    }

    public void commandExecute(LinkedHashSet<String> commandSet, HttpServletRequest request) throws Exception {
        for (String commandName : commandSet) {
            if ("".equals(commandName) || "null".equals(commandName)) {
                continue;
            }
            Command command = (Command) springUtils.getBean(commandName);
            Object o = request.getParameterMap();;
            command.execute(request, o);
        }
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

    @Override
    public List<String> getPathPatterns() {
        return pathPatterns;
    }

    public void setPathPatterns(List<String> pathPatterns) {
        this.pathPatterns = pathPatterns;
    }

    @Override
    public List<String> getExcludePathPatterns() {
        return excludePathPatterns;
    }

    public void setExcludePathPatterns(List<String> excludePathPatterns) {
        this.excludePathPatterns = excludePathPatterns;
    }

    @Autowired
    public void setSpringUtils(SpringUtils springUtils) {
        this.springUtils = springUtils;
    }
}
