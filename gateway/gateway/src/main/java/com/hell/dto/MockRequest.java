package com.hell.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class MockRequest implements Serializable {
    private String mockInfo;
}
