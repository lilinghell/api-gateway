package com.hell.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Utils {
    private static final Log logger = LogFactory.getLog(Utils.class);

    // 定义jackson对象
    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * 对象装json字符串
     */
    public static String objectToJson(Object data) {
        String string = "";
        try {
            string = mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            logger.error("objectToJson is error");
        }
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
