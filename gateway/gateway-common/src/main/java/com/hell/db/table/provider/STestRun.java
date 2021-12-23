package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;

@Data
@Entity(name = "s_test_run")
public class STestRun extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer caseSeq;
    @Column(nullable = false)
    private Integer appSeq;
    @Column
    private Integer autoCaseSeq;
    @Column(columnDefinition = "char(1) comment '0:手动 1:自动'")
    private String runType;
    @Column(columnDefinition = "char(1) comment '0:执行成功 1:执行失败'")
    private String status;
    @Lob
    @Column
    private String requestParameter;
    @Lob
    @Column
    private String responseResult;

    @Column(length = 12)
    private String execTime;
}
