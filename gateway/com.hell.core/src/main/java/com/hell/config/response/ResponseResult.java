package com.hell.config.response;


import java.io.Serializable;

public class ResponseResult<T> implements Serializable, Response {
    private String code = "000000";
    private String msg = "success";

    private T data;

    public ResponseResult() {
    }

    public ResponseResult(T data) {
        this.data = data;
    }

    public ResponseResult(String code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

