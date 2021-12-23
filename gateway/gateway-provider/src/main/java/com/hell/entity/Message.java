package com.hell.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {
    private String type;//类型 0：router通知、1:testRunResult 通知
    private Object val;//值
}
