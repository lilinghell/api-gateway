package com.hell.entity.tag;

import lombok.Data;

import java.io.Serializable;

@Data
public class QryApiByTagResBean implements Serializable {
    private String apiUrl;
    private String apiName;

    QryApiByTagResBean(String url, String name) {
        this.apiUrl = url;
        this.apiName = name;
    }
}
