package com.hell.db.table.op;

import com.hell.db.table.LogicDelBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "m_role")
@org.hibernate.annotations.Table(appliesTo = "m_role", comment = "角色表")
public class MRole extends LogicDelBaseEntity {

    @Column(unique = true, columnDefinition = "varchar(64) comment '角色名'")
    private String roleName;

    @Lob
    @Column
    private String routers;
}
