package com.hell.core.common.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class Utils {
    // 定义jackson对象
    private static final ObjectMapper mapper = new ObjectMapper();

    private static String cleanContextPath(String contextPath) {
        String candidate = StringUtils.trimWhitespace(contextPath);
        return StringUtils.hasText(candidate) && candidate.endsWith("/") ? candidate.substring(0, candidate.length() - 1) : candidate;
    }

    /**
     * 对象装json字符串
     */
    public static String objectToJson(Object data) throws JsonProcessingException {
        String string = mapper.writeValueAsString(data);
        return string;
    }

    /**
     * json字符串转对象
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) throws JsonProcessingException {
        T t = mapper.readValue(jsonData, beanType);
        return t;
    }

    /**
     * 将json数据转换成对象list
     */
    public static <T> List<T> jsonToList(String jsonData, Class<T> beanType) throws JsonProcessingException {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, beanType);
        List<T> list = mapper.readValue(jsonData, javaType);
        return list;
    }

    public static void main(String[] args) throws IOException {
        String ok = "{\n" +
                "  \"msg\": \"string\",\n" +
                "  \"code\": \"string002\",\n" +
                "  \"data\": {\n" +
                "    \"name\": \"string\",\n" +
                "    \"id\": 1,\n" +
                "    \"status\": \"string\"\n" +
                "  }\n" +
                "}";
        HashMap re = jsonToPojo(ok, HashMap.class);

        Object chekVal = "";
        String str = "code";
        String[] key = str.split("\\.");

        for (int i = 0; i < key.length; i++) {
            if (re.get(key[i]) instanceof HashMap) {
                re = (HashMap) re.get(key[i]);
            } else {
                chekVal = re.get(key[i]);
            }
        }
        System.out.println(chekVal);
    }

}
