package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "s_team")
@org.hibernate.annotations.Table(appliesTo = "s_team", comment = "团队信息表")
public class STeam extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;

    @Column(nullable = false, columnDefinition = "varchar(64) comment '团队名称'")
    private String teamName;

    @Column(columnDefinition = "varchar(256) comment '描述'")
    private String info;

    @Column(columnDefinition = "char(1) comment '状态 0：正常、1：注销'")
    private String status;

    @Column(nullable = false)
    private Integer leaderSeq;
}
