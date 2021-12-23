package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_dept")
@org.hibernate.annotations.Table(appliesTo = "s_dept", comment = "部门表")
public class SDept extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "parentSeq", referencedColumnName = "seq")
    private SDept parent;

    @Column(nullable = false)
    private Integer entSeq;

    @Column(nullable = false, columnDefinition = "varchar(64) comment '部门名称'")
    private String deptName;

    @Column(columnDefinition = "varchar(256) comment '描述'")
    private String info;

    @Column(columnDefinition = "char(1) comment '状态 0：正常、1：注销'")
    private String status;
}
