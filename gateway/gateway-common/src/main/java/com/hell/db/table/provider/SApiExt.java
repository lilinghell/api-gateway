package com.hell.db.table.provider;

import lombok.Data;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_api_ext")
public class SApiExt {
    @Id
    private Integer apiSeq;
    @Column
    private Integer entSeq;
    @Column(columnDefinition = "varchar(10) comment 'connectTimeout'")
    private String connectTimeout;
    @Column(columnDefinition = "varchar(10) comment 'responseTimeout'")
    private String responseTimeout;
    @Column(columnDefinition = "varchar(10) comment '补充令牌速度'")
    private String replenishRate;
    @Column(columnDefinition = "varchar(10) comment '令牌桶容量'")
    private String burstCapacity;
    @Type(type = "org.hibernate.type.TextType")
    @Column
    private String apiMock;
}
