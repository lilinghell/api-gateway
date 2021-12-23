package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "s_app_env")
@org.hibernate.annotations.Table(appliesTo = "s_app_env", comment = "生态环境表")
public class SAppEnv extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer appSeq;

    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, columnDefinition = "char(12) comment '类型：DEV、TEST、UAT、PRO'")
    private String type;
    @Column(length = 32)
    private String envKey;
    @Column(nullable = false, columnDefinition = "char(1) comment '0:HTTP 1:微服务'")
    private String serviceType;

    @Column(columnDefinition = "varchar(512) comment '地址'")
    private String address;

    @Column(length = 256)
    private String info;
}
