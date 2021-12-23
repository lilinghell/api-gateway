package com.hell.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TestCaseRunResult implements Serializable {
    private Integer testRunSeq;
    private Integer caseSeq;
    private String status;
    private Date updateTime;
}
