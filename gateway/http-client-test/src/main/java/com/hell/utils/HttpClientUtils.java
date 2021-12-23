package com.hell.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.Consts;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Component
public class HttpClientUtils {
    private static CloseableHttpClient httpClient;
    private static String token = "";

    public static final String HOST_PATH = "http://localhost:8090";
    public static final String LOGIN_PATH = "/fbs-app/loginManage/login";
    public static final String TOKEN_NAME = "token";

    public static final String GET_CONTENT_TYPE = "text/html;charset=UTF-8";
    public static final String POST_CONTENT_TYPE = "application/json;charset=UTF-8";

    public static <T> String doGet(String url, Object paramsObj) throws IOException, URISyntaxException {
        Map<String, String> params = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramsObj), Map.class);
        return doGet(url, params, -1);
    }

    public static <T> String doGet(String url, Object paramsObj, int socketTimeout) throws IOException, URISyntaxException {
        Map<String, String> params = JsonUtils.jsonToPojo(JsonUtils.objectToJson(paramsObj), Map.class);
        return doGet(url, params, socketTimeout);
    }

    public static <T> T doPost(String url, Object paramsObj, Class<T> responseType) throws IOException {
        return doPost(url, paramsObj, responseType, -1);
    }

    public static String doPost(String url, Object paramsObj) throws IOException {
        return doPost(url, paramsObj, -1);
    }

    public static <T> T doPost(String url, Object paramsObj, Class<T> responseType, int socketTimeout)
            throws IOException {
        String responseContent = doPost(url, paramsObj, socketTimeout);
        if (StringUtils.isBlank(responseContent)) {
            return null;
        }

        T response = JsonUtils.jsonToPojo(responseContent, responseType);

        return response;
    }

    public static String doPost(String url, Object paramsObj, int socketTimeout) throws IOException {
        HttpPost post = new HttpPost(HOST_PATH + url);
        String paramsStr = paramsObj instanceof String ? (String) paramsObj : JsonUtils.objectToJson(paramsObj);
        StringEntity entity = new StringEntity(paramsStr, "UTF-8");
        post.addHeader(HttpHeaders.CONTENT_TYPE, POST_CONTENT_TYPE);
        //设置token值
        post.addHeader(TOKEN_NAME, token);
        post.setEntity(entity);
        return doHttp(post, socketTimeout);
    }

    public static String doGet(String url, Map<String, String> params) throws IOException, URISyntaxException {
        return doGet(url, params, -1);
    }

    public static String doGet(String url, Map<String, String> params, int socketTimeout) throws IOException, URISyntaxException {
        URIBuilder uriBuilder = new URIBuilder(HOST_PATH + url);
        uriBuilder.setCharset(Consts.UTF_8).build();
        if (params != null) {
            params.forEach(uriBuilder::addParameter);
        }
        HttpGet get = new HttpGet(uriBuilder.build());
        //设置请求头
        get.addHeader(HttpHeaders.CONTENT_TYPE, GET_CONTENT_TYPE);
        //设置token值
        get.addHeader(TOKEN_NAME, token);
        return doHttp(get, socketTimeout);
    }


    private static String doHttp(HttpRequestBase request, int socketTimeout) throws IOException {
        if (socketTimeout > 0) {
            //获取原有配置
            Configurable configClient = (Configurable) httpClient;
            RequestConfig.Builder custom = RequestConfig.copy(configClient.getConfig());
            //设置个性化配置
            RequestConfig config = custom.setSocketTimeout(socketTimeout).build();
            request.setConfig(config);
        }
        String response = "";
        if (request.getURI().getPath().equals(LOGIN_PATH)) {
            //登录获取token,暂时存放为全局变量
            CloseableHttpResponse re = httpClient.execute(request);
            String tokenValue = re.getFirstHeader(TOKEN_NAME).getValue();
            token = tokenValue;
            if (re.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                response = EntityUtils.toString(re.getEntity(), "UTF-8");
            }
        } else {
            ResponseHandler<String> handler = new BasicResponseHandler();
            response = httpClient.execute(request, handler);
        }
        return response;
    }

    @Autowired
    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
