package com.hell.entity.route;

import lombok.Data;

import java.io.Serializable;

@Data
public class NotifyApiRoute implements Serializable {
    private String envKey;
    private Integer envSeq;
    private Integer appEnvSeq;
    private String serviceType;
    private String url;
    private String status;
    private String flowSwitch;
    private String mockSwitch;
    private Integer apiSeq;
    private String apiMock;
    private String connectTimeout;
    private String replenishRate;
    private String responseTimeout;
    private String burstCapacity;
    private String registryType;
    private String address;
    private Integer entSeq;
    private String serviceId;
}
