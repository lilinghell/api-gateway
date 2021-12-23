package com.hell.entity.route;

import lombok.Data;

import java.io.Serializable;

@Data
public class AckApiRoute implements Serializable {
    //类型：delete、refresh
    private String type;
    private Integer apiSeq;
    private Integer entSeq;
    private String envKey;
    private String apiUrl;
    //状态：成功、失败
    private boolean status;
    private String errorMsg;
    private String hostIp;
}
