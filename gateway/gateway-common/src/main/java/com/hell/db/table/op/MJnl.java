package com.hell.db.table.op;


import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "m_jnl", indexes = {@Index(name = "m_jnl_user", columnList = "userSeq")})
@org.hibernate.annotations.Table(appliesTo = "m_jnl", comment = "流水表")
public class MJnl extends BaseEntity {
    @Column(columnDefinition = "varchar(256) comment 'api路径'")
    private String apiPath;

    @Column(columnDefinition = "integer comment '用户seq'")
    private Integer userSeq;

    @Column(columnDefinition = "char(1) comment '交易状态：0：成功，1：失败，9：处理中'")
    private String jnlStatus;

    @Lob
    @Column
    private String jnlData;

    @Column(columnDefinition = "varchar(128) comment '错误码'")
    private String errorCode;

    @Column(columnDefinition = "varchar(256) comment '错误信息'")
    private String errorMsg;

    @Column(columnDefinition = "varchar(256) comment '其它信息'")
    private String otherMsg;

}
