package com.hell.db.table.provider;

import com.hell.db.table.LogicDelBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "s_role")
@org.hibernate.annotations.Table(appliesTo = "s_role", comment = "角色表")
public class SRole extends LogicDelBaseEntity {

    @Column(columnDefinition = "varchar(64) comment '角色名'")
    private String roleName;

    @Lob
    @Column
    private String routers;

    private Integer entSeq;
}
