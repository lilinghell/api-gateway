package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_parameter")
@org.hibernate.annotations.Table(appliesTo = "s_parameter", comment = "参数变量表")
public class SParameter extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, length = 24)
    private String enName;
    @Column(nullable = false, length = 64)
    private String value;
    @Column(nullable = false, columnDefinition = "varchar(1) comment '启用状态：0常量、1变量'")
    private String type;
}
