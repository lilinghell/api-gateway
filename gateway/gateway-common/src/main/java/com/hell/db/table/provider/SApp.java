package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "s_app")
@org.hibernate.annotations.Table(appliesTo = "s_app", comment = "应用表")
public class SApp extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;

    @Column(nullable = false, length = 32)
    private String name;
    @Column(nullable = false, columnDefinition = "char(1) comment '类型：0j2ee、1web、2Microservices、3batch'")
    private String type;

    @Column(length = 256)
    private String info;
}
