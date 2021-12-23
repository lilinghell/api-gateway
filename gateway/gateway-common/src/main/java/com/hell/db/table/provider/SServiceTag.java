package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
@Entity(name = "s_service_tag")
public class SServiceTag extends BaseEntity {
    @Column(nullable = false, length = 12)
    private String name;
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false)
    private Integer entSeq;
    @Column(length = 12)
    private String color;
}
