package com.hell.entity.api;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ApiTable {

    //接口名称
    private String apiName;
    //接口描述
    private String description;
    //url
    private String url;
    //method
    private String method;
    //version
    private String version;
    //请求方式
    private List<String> consumes;
    //应答方式
    private List<String> produces;
    //请求体
    private List<ApiParameter> parametersList;
    //返回体
    private List<ApiParameter> responsesList;
    //请求示例
    private List<String> requestDemo;
    private Map<String, Object> requestObjectDemo;
    //应答示例
    private Map<String, Object> responseDemo;
    private String serviceId;
}