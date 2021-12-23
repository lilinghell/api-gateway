package com.hell.db.table.provider;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "s_api_group")
public class SApiGroup extends BaseEntity {
    @Column(nullable = false)
    private Integer entSeq;
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false)
    private String name;

    @Column(length = 256)
    private String info;

    @OneToOne
    @JoinColumn(name = "parentSeq", referencedColumnName = "seq")
    private SApiGroup parent;
}
