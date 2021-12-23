package com.hell.db.table.common;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "p_routers")
@org.hibernate.annotations.Table(appliesTo = "p_routers", comment = "路由信息")
public class PRouters extends BaseEntity {

    @Column(nullable = false, columnDefinition = "char(1) comment '服务类型0：服务提供商 1：服务开发商'")
    private String serviceType;

    @Lob
    @Column
    private String routers;
}
