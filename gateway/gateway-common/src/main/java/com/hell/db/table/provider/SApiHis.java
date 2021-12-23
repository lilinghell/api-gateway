package com.hell.db.table.provider;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "s_api_his")
@org.hibernate.annotations.Table(appliesTo = "s_api_his", comment = "接口历史表")
public class SApiHis implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer seq;
    private Integer appEnvSeq;
    private Date createTime;
    private Date updateTime;
    private Integer entSeq;
    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, length = 12)
    private String version;
    @Column(nullable = false, columnDefinition = "varchar(12) comment 'openapi_3.0'")
    private String schemaType;
    @Column(nullable = false, columnDefinition = "varchar(12) comment 'YAML、JSON'")
    private String schemaFormat;
    @Column(length = 64)
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

    @Column(columnDefinition = "varchar(1) comment '同步网关：0同步、1未同步'")
    private String syncGateway;
}
