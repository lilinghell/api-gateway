package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "s_test_cases")
@org.hibernate.annotations.Table(appliesTo = "s_test_cases", comment = "测试用例表")
public class STestCases extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false, length = 32)
    private String name;
    private Integer uniKey;
    @Column(columnDefinition = "char(1) comment '0:正例 1:反例 2:基础用例'")
    private String caseType;
    @Column(columnDefinition = "char(1) comment '0:执行成功1:执行失败2:待执行3:执行中4:验证成功5:验证失败'")
    private String status;
    @Column(nullable = false, length = 128)
    private String apiPath;
    @Lob
    @Column
    private String parameter;
    @Lob
    @Column
    private String assertScript;

    @Column(nullable = false)
    private Integer testGroupSeq;

    @Column(length = 256)
    private String caseDesc;
}
