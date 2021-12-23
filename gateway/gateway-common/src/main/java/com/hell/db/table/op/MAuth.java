package com.hell.db.table.op;


import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "m_auth")
@org.hibernate.annotations.Table(appliesTo = "m_auth", comment = "授权表")
public class MAuth extends BaseEntity {
    @Column(columnDefinition = "varchar(256) comment 'api路径'")
    private String apiPath;

    @Column(columnDefinition = "integer comment '制单员seq'")
    private Integer operatorSeq;

    @Column(columnDefinition = "char(1) comment '授权状态：0：通过，1：拒绝，9：待审核'")
    private String authStatus;

    @Lob
    @Column
    private String authData;

    @Column(columnDefinition = "integer comment '审核seq'")
    private Integer authOperatorSeq;

    @Column(columnDefinition = "date comment '制单时间'")
    private Date trsTime;

    @Column(columnDefinition = "date comment '审核时间'")
    private Date authTime;

    @Column(columnDefinition = "varchar(256) comment '信息'")
    private String msg;
}
