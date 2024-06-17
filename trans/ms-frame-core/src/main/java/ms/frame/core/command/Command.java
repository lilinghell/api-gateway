package ms.frame.core.command;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface Command {
    boolean execute(HttpServletRequest request, HttpServletResponse response, Map data) throws Exception;
}
