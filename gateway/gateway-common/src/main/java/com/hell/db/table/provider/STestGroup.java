package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Data
@Entity(name = "s_test_group")
public class STestGroup extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "parentSeq", referencedColumnName = "seq")
    private STestGroup parent;

    @Column(nullable = false)
    private Integer entSeq;

    @Column(nullable = false)
    private Integer appSeq;

    @Column(nullable = false, columnDefinition = "varchar(64) comment '测试集合名称'")
    private String name;

    @Column(columnDefinition = "varchar(256) comment '描述'")
    private String info;
}
