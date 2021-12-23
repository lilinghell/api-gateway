package com.hell.entity.api;

import lombok.Data;

import java.io.Serializable;

@Data
public class AckApiRoute implements Serializable {
    //类型：delete、refresh
    private String type;
    private Integer apiSeq;
    private String apiUrl;
    private String envKey;
    private Integer entSeq;
    //状态：成功、失败
    private boolean status;
    private String errorMsg;
    private String hostIp;
}
