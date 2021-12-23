package com.hell.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtils {
    // 定义jackson对象
    private static final ObjectMapper mapper = new ObjectMapper();


    /**
     * 对象转json字符串
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
}
