package com.hell.entity.api;

import lombok.Data;

import java.util.Map;

@Data
public class ApiParameter {

    //请求参数描述
    private String description = "";
    //参数名
    private String name;
    //是否必输
    private boolean required;
    //类型 string, number, boolean, integer, array
    private String type;
    //格式
    private String format = "";
    //body, header, formData, query, path
    private String in;
    //其他信息，如最小值，最大值等
    private Map<String, Object> otherInfo;
}
