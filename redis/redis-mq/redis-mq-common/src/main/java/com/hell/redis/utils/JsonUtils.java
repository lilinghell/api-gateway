package com.hell.redis.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class JsonUtils {
    // 定义jackson对象
    private static final ObjectMapper mapper = new ObjectMapper();

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

    private static <T> T objectToPojo(Object object, Class<T> beanType) throws IOException {
        if (object instanceof byte[]) {
            return (T) byteToPojo((byte[]) object, beanType);
        }
        if (object instanceof String) {
            return (T) jsonToPojo((String) object, beanType);
        }
        return null;
    }

    public static <T> T byteToPojo(byte[] bytes, Class<T> beanType) throws IOException {
        T t = mapper.readValue(bytes, beanType);
        return t;
    }

    public static <T> T inputStreamToPojo(InputStream inputStream, Class<T> beanType) throws IOException {
        T t = mapper.readValue(inputStream, beanType);
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

    /**
     * 将List<<Map>>  转对象list
     */
    public static <T> List<T> objectToList(Object data, Class<T> beanType) throws JsonProcessingException {
        return jsonToList(objectToJson(data), beanType);
    }

    public static void main(String[] args) throws IOException {
        String ok = "{\n" + "  \"msg\": \"string\",\n" + "  \"code\": \"string002\",\n" + "  \"data\": {\n" + "    \"name\": \"string\",\n" + "    \"id\": 1,\n" + "    \"status\": \"string\"\n" + "  }\n" + "}";
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
