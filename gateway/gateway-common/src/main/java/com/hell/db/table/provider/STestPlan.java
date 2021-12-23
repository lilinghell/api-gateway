package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "s_test_plan")
public class STestPlan extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, length = 128)
    private String planTime;
    @Column(nullable = false, length = 128)
    private String planRule;
    @Column(nullable = false)
    private Boolean usingFlg;
    @Column(length = 256)
    private String planInfo;
}
