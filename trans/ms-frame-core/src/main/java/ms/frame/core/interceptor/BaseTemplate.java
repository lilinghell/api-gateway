package ms.frame.core.interceptor;

import ms.frame.core.command.Command;
import ms.frame.core.context.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

public class BaseTemplate implements BaseHandlerInterceptor {
    private final static String encoding = "UTF-8";
    private SpringUtils springUtils;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private LinkedHashSet<String> commandsPre = new LinkedHashSet<String>();
    private LinkedHashSet<String> commandsPost = new LinkedHashSet<String>();
    private LinkedHashSet<String> commandsAfter = new LinkedHashSet<String>();
    private List<String> pathPatterns = new ArrayList<>();
    private List<String> excludePathPatterns = new ArrayList<String>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return commandExecute(commandsPre, request, response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        commandExecute(commandsPost, request, response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        commandExecute(commandsAfter, request, response);
    }

    public boolean commandExecute(LinkedHashSet<String> commandSet, HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean b = true;
        for (String commandName : commandSet) {
            if ("".equals(commandName) || "null".equals(commandName)) {
                continue;
            }
            Command command = (Command) springUtils.getBean(commandName);
            b = command.execute(request, response, request.getParameterMap());
            if (!b) {
                break;
            }
        }

        if (!b) {
//            byte[] json = null;
//            if (this.log.isTraceEnabled()) {
//                this.log.trace("json bytes: " + new String(json, this.encoding));
//            }
//
//            OutputStream out = response.getOutputStream();
//            response.setContentLength(json.length);
//            out.write(json);
//            out.flush();
        }
        return b;
    }
/*
    private Map getBody(HttpServletRequest request) {
        int contentLength = request.getContentLength();
        byte[] buffer = new byte[contentLength];
        ServletInputStream in = null;
        String bodyData = "";
        Map map = null;
        try {
            in = request.getInputStream();
            in.read(buffer, 0, contentLength);
            in.close();
            bodyData = new String(buffer, StandardCharsets.UTF_8);
            map = JsonUtils.jsonToPojo(bodyData, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }*/

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
