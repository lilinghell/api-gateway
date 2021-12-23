package com.hell.db.table.op;

import com.hell.db.table.BaseEntity;
import com.hell.db.table.common.PAttachment;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "m_user")
@org.hibernate.annotations.Table(appliesTo = "m_user", comment = "平台运营商-用户表")
public class MUser extends BaseEntity {

    @Column(unique = true, columnDefinition = "varchar(64) comment '用户id'")
    private String userId;

    @Column(columnDefinition = "varchar(128) comment '用户姓名'")
    private String userName;

    @Column(nullable = false, columnDefinition = "char(1) comment '用户状态:0：正常、1：锁定、2：停用'")
    private String userState;

    @OneToOne
    @JoinColumn(name = "roleSeq", referencedColumnName = "seq")
    private MRole role;

    @Column(columnDefinition = "varchar(64) comment '登陆密码'")
    private String password;

    @Column(unique = true, columnDefinition = "varchar(64) comment '邮箱'")
    private String email;

    @Column(unique = true, columnDefinition = "varchar(20) comment '手机'")
    private String mobilePhone;

    @Column(columnDefinition = "integer comment '创建者'")
    private Integer createSeq;

    @OneToOne
    @JoinColumn(name = "attachmentSeq", referencedColumnName = "seq")
    private PAttachment headIcon;

    @Column(columnDefinition = "char(1) default '2' comment '性别: 0:男 1:女 2:保密'")
    private String gender;
}
