package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "s_message")
@org.hibernate.annotations.Table(appliesTo = "s_message", comment = "消息表")
public class SMessage extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false, columnDefinition = "varchar(1) comment '0:route'")
    private String type;
    @Column(nullable = false, length = 64)
    private String title;
    @Column(nullable = false, columnDefinition = "varchar(1) comment '0已阅、1未阅'")
    private String status;
    @Lob
    @Column
    private String info;
}
