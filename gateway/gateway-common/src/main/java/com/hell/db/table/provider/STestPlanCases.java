package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "s_test_plan_cases")
public class STestPlanCases extends BaseEntity {
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer testPlanSeq;
    @Column(nullable = false)
    private Integer testCaseSeq;
}
