package ms.frame.core.http;

import ms.frame.core.common.JsonUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * application/json下request.getInputStream()可多次读取
 */
public class HttpServletRequestWrapperExt extends HttpServletRequestWrapper {
    private byte[] body = null;
    private String contentType = "";
    private Map<String, String[]> bodyMap = null;

    public HttpServletRequestWrapperExt(HttpServletRequest request) throws IOException {
        super(request);
        contentType = StringUtils.lowerCase(request.getContentType());
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            this.body = StreamUtils.copyToByteArray(request.getInputStream());
            if (ObjectUtils.isNotEmpty(this.body)) {
                this.bodyMap = JsonUtils.byteToPojo(this.body, Map.class);
            }
        }
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            return this.bodyMap;
        } else {
            return super.getParameterMap();
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        } else{
            return super.getInputStream();
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
}
