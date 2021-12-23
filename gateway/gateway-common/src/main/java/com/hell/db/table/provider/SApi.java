package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_api")
@org.hibernate.annotations.Table(appliesTo = "s_api", comment = "接口表")
public class SApi extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;

    @Column(nullable = false)
    private Integer appEnvSeq;

    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, length = 12)
    private String version;
    @Column(nullable = false, columnDefinition = "varchar(12) comment 'openapi_3.0'")
    private String schemaType;
    @Column(nullable = false, columnDefinition = "varchar(12) comment 'YAML、JSON'")
    private String schemaFormat;
    @Column(nullable = false, length = 64)
    private String url;
    @Column(length = 32)
    private String method;
    @Column(nullable = false, columnDefinition = "varchar(1) comment '启用状态：0启用、1停用'")
    private String status;
    @Column(nullable = false, columnDefinition = "varchar(1) comment 'mock开关：0启用、1停用'")
    private String mockSwitch;
    @Column(nullable = false, columnDefinition = "varchar(1) comment '限流开关：0启用、1停用'")
    private String flowSwitch;
    @Column(length = 128)
    private String detail;
    @Lob
    @Column
    private String info;

    @OneToOne
    @JoinColumn(name = "groupSeq", referencedColumnName = "seq")
    private SApiGroup group;

    @Column(columnDefinition = "varchar(1) comment '同步网关：0同步、1未同步'")
    private String syncGateway;

    @Column(length = 128)
    private String serviceId;
}
