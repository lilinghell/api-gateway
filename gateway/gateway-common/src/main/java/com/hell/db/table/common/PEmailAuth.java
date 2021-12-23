package com.hell.db.table.common;

import com.hell.db.table.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "p_email_auth")
@org.hibernate.annotations.Table(appliesTo = "p_email_auth", comment = "邮箱服务表")
public class PEmailAuth extends BaseEntity {
    @Column(nullable = false, columnDefinition = "varchar(128) comment '交易码'")
    private String apiPath;
    @Column(nullable = false, columnDefinition = "varchar(128) comment '用户邮箱'")
    private String email;
    @Column(nullable = false, columnDefinition = "varchar(12) comment '邮箱认证码'")
    private String emailCode;
}
