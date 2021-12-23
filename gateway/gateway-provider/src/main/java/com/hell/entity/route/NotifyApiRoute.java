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

    public String getEnvKey() {
        return envKey;
    }

    public void setEnvKey(String envKey) {
        this.envKey = envKey;
    }

    public Integer getEnvSeq() {
        return envSeq;
    }

    public void setEnvSeq(Integer envSeq) {
        this.envSeq = envSeq;
    }

    public Integer getAppEnvSeq() {
        return appEnvSeq;
    }

    public void setAppEnvSeq(Integer appEnvSeq) {
        this.appEnvSeq = appEnvSeq;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFlowSwitch() {
        return flowSwitch;
    }

    public void setFlowSwitch(String flowSwitch) {
        this.flowSwitch = flowSwitch;
    }

    public String getMockSwitch() {
        return mockSwitch;
    }

    public void setMockSwitch(String mockSwitch) {
        this.mockSwitch = mockSwitch;
    }

    public Integer getApiSeq() {
        return apiSeq;
    }

    public void setApiSeq(Integer apiSeq) {
        this.apiSeq = apiSeq;
    }

    public String getApiMock() {
        return apiMock;
    }

    public void setApiMock(String apiMock) {
        this.apiMock = apiMock;
    }

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(String connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String getReplenishRate() {
        return replenishRate;
    }

    public void setReplenishRate(String replenishRate) {
        this.replenishRate = replenishRate;
    }

    public String getResponseTimeout() {
        return responseTimeout;
    }

    public void setResponseTimeout(String responseTimeout) {
        this.responseTimeout = responseTimeout;
    }

    public String getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(String burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getEntSeq() {
        return entSeq;
    }

    public void setEntSeq(Integer entSeq) {
        this.entSeq = entSeq;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
