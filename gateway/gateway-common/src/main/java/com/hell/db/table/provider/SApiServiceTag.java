package com.hell.db.table.provider;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "s_api_service_tag")
@IdClass(SApiServiceTag.class)
public class SApiServiceTag implements Serializable {
    @Column(nullable = false)
    private Integer appSeq;
    @Column(nullable = false)
    private Integer entSeq;
    @Id
    @Column(nullable = false)
    private Integer apiSeq;
    @Id
    @Column(nullable = false)
    private Integer serviceTagSeq;
}
